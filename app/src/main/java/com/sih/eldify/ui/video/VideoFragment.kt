package com.sih.eldify.ui.video

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.sih.eldify.R
import com.sih.eldify.utils.SOS
import com.sih.eldify.websockets.EchoWebSocketListener
import kotlinx.android.synthetic.main.fragment_bot.*
import kotlinx.android.synthetic.main.fragment_sos.*
import kotlinx.android.synthetic.main.fragment_video.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.json.JSONObject
import java.security.MessageDigest
import java.util.*


class VideoFragment : Fragment() {

    companion object {
        fun newInstance() = VideoFragment()
    }

    private lateinit var viewModel: VideoViewModel

    private var SOCKET_URL_BOT: String? = null
    private var IP_ADDR_1: String? = null
    private var IP_ADDR_2: String? = null

    private val client by lazy {
        OkHttpClient()
    }

    var wsSOS: WebSocket? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VideoViewModel::class.java)
        // TODO: Use the ViewModel

        val sharedPreferences = activity?.getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)
        val sharedPreferencesIP = activity?.getSharedPreferences("IP_CONNECTION", Context.MODE_PRIVATE)

        setIPAddress(sharedPreferencesIP)
        setURLBot()

        val uniqueString = sharedPreferences?.getString("USER_EMAIL", null) + sharedPreferences?.getString("EM_CONTACT_1", null)
        val hashString = md5(uniqueString)

            join_btn.setOnClickListener {
            if (hashString.isNotEmpty()) {
                val options = JitsiMeetConferenceOptions.Builder()
                    .setRoom(hashString)
                    .build()
                JitsiMeetActivity.launch(requireView().context, options)
            }
        }
    }

    private fun callNumber(phoneNumber: String?) {
        val phone_intent = Intent(Intent.ACTION_CALL)

        phone_intent.data = Uri.parse(
            "tel:$phoneNumber"
        )
        startActivity(phone_intent)
    }

    private fun sendSMS(phoneNumber: String?, reason: String) {
        try {

            // on below line we are initializing sms manager.
            val smsManager: SmsManager = SmsManager.getDefault()

            // on below line we are sending text message.
            smsManager.sendTextMessage(phoneNumber, null, reason, null, null)

            // on below line we are displaying a toast message for message send,
            Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show()

        } catch (e : Exception) {

            // on catch block we are displaying toast message for error.
            Toast.makeText(context, "Please enter all the data.."+e.message.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun md5(toEncrypt: String): String {
        return try {
            val digest: MessageDigest = MessageDigest.getInstance("md5")
            digest.update(toEncrypt.toByteArray())
            val bytes: ByteArray = digest.digest()
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(String.format("%02X", bytes[i]))
            }
            sb.toString().lowercase(Locale.getDefault())
        } catch (exc: Exception) {
            "" // Impossibru!
        }
    }

    private fun setIPAddress(sharedPreferences: SharedPreferences?) {
        if(sharedPreferences?.getString("IP_1", null) != null && sharedPreferences.getString("IP_2", null) != null) {
            IP_ADDR_1 = sharedPreferences?.getString("IP_1", null)
            IP_ADDR_2 = sharedPreferences?.getString("IP_2", null)
        }else{
            createIPSetBuilder(sharedPreferences)
        }
    }



    private fun setURLBot() {
        if(IP_ADDR_1 != null && IP_ADDR_2 != null){
            // 192.168. + IP_ADDR_1 + . + IP_ADDR_2 + : + Port
            SOCKET_URL_BOT = "ws://192.168.$IP_ADDR_1.$IP_ADDR_2:80"
            Log.d("chk", SOCKET_URL_BOT!!)
            start()
        }
    }

    override fun onResume() {
        super.onResume()
//        start()
    }

    override fun onPause() {
        super.onPause()
        stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        client.dispatcher.executorService.shutdown()
    }

    override fun onStop() {
        super.onStop()
        stop()
    }

    private fun start() {

        if(SOCKET_URL_BOT != null){
            val requestSOS: Request = Request.Builder().url("$SOCKET_URL_BOT/COM").build()
            val listenerSOS =
                EchoWebSocketListener(this::outputSOS, this::ping, this::setConnectionStatus) {
                    wsSOS = null
                }
            wsSOS = client.newWebSocket(requestSOS, listenerSOS)
            Log.d("Debug", "Start executed")
        }
    }

    private fun stop() {
        wsSOS?.close(EchoWebSocketListener.NORMAL_CLOSURE_STATUS, "Connection Closed!")
    }

    private fun ping(s: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendJSONOnSOS(command: String, text: String) {
        wsSOS?.apply {
            var jsonObj = JSONObject()
            jsonObj.put("SOS", command)
            send(jsonObj.toString())
//            Log.d("DEBUG", jsonObj.toString())
            tv_controller_command.text = text
        } ?: ping("Error: Restart the App to reconnect")
    }

    private fun outputSOS(text: String) {
        activity?.runOnUiThread {
//            sos.setText(text)
            if(text.contains("recieved")){
                val gson = Gson()
                val sos: SOS = gson.fromJson(text, SOS::class.java)
                Log.d("tanvi","> From JSON String:\n" + sos)

                val sharedPreferencesBS = activity?.getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)
                val number_1 = sharedPreferencesBS?.getString("EM_CONTACT_1", null)
                val number_2 = sharedPreferencesBS?.getString("EM_CONTACT_2", null)

                sendSMS(number_1, sos.reason)
                sendSMS(number_2, sos.reason)

                callNumber(number_1)
            }
        }
    }

    fun setConnectionStatus(txt: String) {
        activity?.runOnUiThread {
//            if (txt == "Disconnected!") {
//                status.setTextColor(Color.RED)
//            } else {
//                status.setTextColor(Color.GREEN)
//            }
//            status.text = txt

        }
    }


    private fun createIPSetBuilder(sharedPreferences : SharedPreferences?){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Ip Reconnect")

        // set the custom layout
        val ipLayout: View = layoutInflater
            .inflate(
                R.layout.ip_connection_layout,
                null
            )
        builder.setView(ipLayout)

        // add a button
        builder
            .setPositiveButton(
                "Reconnect",
                DialogInterface.OnClickListener { dialog, which -> // send data from the
                    // AlertDialog to the Activity

                    val ip_1: EditText = ipLayout.findViewById(R.id.et_connection_ip_addr_1)
                    val ip_2: EditText = ipLayout.findViewById(R.id.et_connection_ip_addr_2)
                    IP_ADDR_1 = ip_1.text.toString()
                    IP_ADDR_2 = ip_2.text.toString()

                    val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
                    editor.apply {
                        putString("IP_1", IP_ADDR_1)
                        putString("IP_2", IP_ADDR_2)
                    }.apply()

                    Log.d("chk", IP_ADDR_1 + IP_ADDR_2)
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}