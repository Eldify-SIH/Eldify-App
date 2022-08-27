

package com.sih.eldify.ui.sos

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.sih.eldify.R
import com.sih.eldify.websockets.EchoWebSocketListener
import com.sih.eldify.wsSOS
import kotlinx.android.synthetic.main.fragment_bot.*
import kotlinx.android.synthetic.main.fragment_sos.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject


class SosFragment : Fragment() {

    companion object {
        fun newInstance() = SosFragment()
    }

    private lateinit var viewModel: SosViewModel

    private var SOCKET_URL_BOT: String? = null
    private var IP_ADDR_1: String? = null
    private var IP_ADDR_2: String? = null

    private var timer: CountDownTimer? = null
    private var timerLengthSeconds: Int = 15
    private var secondsRemaining: Int = 15

    private val client by lazy {
        OkHttpClient()
    }

    var wsCOM: WebSocket? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SosViewModel::class.java)
        // TODO: Use the ViewModel

        val sharedPreferences = activity?.getSharedPreferences("IP_CONNECTION", Context.MODE_PRIVATE)

        setIPAddress(sharedPreferences)

        sos_reconnect.setOnClickListener {
            setIPAddress(sharedPreferences)
            start()
        }

        sos_reconnect.setOnLongClickListener {

            createIPSetBuilder(sharedPreferences)

            true
        }

        setURLBot()

        dummy.setOnClickListener { forSOSsent("Test Emergency") }

        textView_countdown.setOnClickListener{
            progress_countdown.setMax(150)
            startTimer()
        }

    }

    private fun setIPAddress(sharedPreferences: SharedPreferences?) {
        if(sharedPreferences?.getString("IP_1", null) != null && sharedPreferences.getString("IP_2", null) != null) {
            IP_ADDR_1 = sharedPreferences?.getString("IP_1", null)
            IP_ADDR_2 = sharedPreferences?.getString("IP_2", null)
            start()
        }else{
            createIPSetBuilder(sharedPreferences)
        }
    }



    private fun setURLBot() {
        if(IP_ADDR_1 != null && IP_ADDR_2 != null){
            SOCKET_URL_BOT = "ws://192.168.$IP_ADDR_1.$IP_ADDR_2:80"
            Log.d("chk", SOCKET_URL_BOT!!)
            start()
        }
    }

    override fun onResume() {
        super.onResume()
        start()
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
                EchoWebSocketListener(this::outputCOM, this::ping, this::setConnectionStatus) {
                    wsCOM = null
                }
            wsCOM = client.newWebSocket(requestSOS, listenerSOS)
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

    private fun sendJSONOnCOM(command: String, text: String) {
        wsCOM?.apply {
            var jsonObj = JSONObject()
            jsonObj.put("COM", command)
            send(jsonObj.toString())
//            Log.d("DEBUG", jsonObj.toString())
            tv_controller_command.text = text
        } ?: ping("Error: Restart the App to reconnect")
    }

    private fun outputCOM(text: String) {
        activity?.runOnUiThread {
            Log.d("txtxtxt", text)
            try {
                val received = JSONObject(text).getString("received")
                val time = JSONObject(text).getString("time")
                val reason = JSONObject(text).getString("reason")

                Log.d("result", received + " " + time + " " + reason)

                val sharedPreferencesBS = activity?.getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)
                val number_1 = sharedPreferencesBS?.getString("EM_CONTACT_1", null)
                val number_2 = sharedPreferencesBS?.getString("EM_CONTACT_2", null)

                sendSMS(number_1, "Reason: $reason")
                sendSMS(number_2, "Reason: $reason")

                callNumber(number_1)

            }catch (exp:Exception ){
                Log.d("Exception is", exp.toString())
            }

        }
    }

    private fun forSOSsent(text: String) {
        Log.d("txtxtxt", text)
        try {
            val received = JSONObject(text).getString("received")
            val time = JSONObject(text).getString("time")
            val reason = JSONObject(text).getString("reason")

            Log.d("result", received + " " + time + " " + reason)

            val sharedPreferencesBS = activity?.getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)
            val number_1 = sharedPreferencesBS?.getString("EM_CONTACT_1", null)
            val number_2 = sharedPreferencesBS?.getString("EM_CONTACT_2", null)

            sendSMS(number_1, "Reason: $reason")
            sendSMS(number_2, "Reason: $reason")

            callNumber(number_1)

        }catch (exp:Exception ){
            Log.d("Exception is", exp.toString())
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

    fun setConnectionStatus(txt: String) {
        activity?.runOnUiThread {
            if(sos_ip_connect != null){
                if (txt == "Disconnected!") {
                    sos_ip_connect.setTextColor(Color.RED)
                } else {
                    sos_ip_connect.setTextColor(Color.GREEN)
                }
                sos_ip_connect.text = txt
            }
        }
    }


    private fun createIPSetBuilder(sharedPreferences : SharedPreferences?){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Ip Reconnect")

        // set the custom layout

        // set the custom layout
        val ipLayout: View = layoutInflater
            .inflate(
                R.layout.ip_connection_layout,
                null
            )
        builder.setView(ipLayout)

        val ip_1: EditText = ipLayout.findViewById(R.id.et_connection_ip_addr_1)
        val ip_2: EditText = ipLayout.findViewById(R.id.et_connection_ip_addr_2)
        val btn : Button = ipLayout.findViewById(R.id.btn_connect_ip)

        val dialog: AlertDialog = builder.create()

        btn.setOnClickListener {
            IP_ADDR_1 = ip_1.text.toString()
            IP_ADDR_2 = ip_2.text.toString()

            val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.apply {
                putString("IP_1", IP_ADDR_1)
                putString("IP_2", IP_ADDR_2)
            }.apply()

            Log.d("chk", IP_ADDR_1 + IP_ADDR_2)
            dialog.dismiss()

        }
        dialog.show()
    }

    private fun onTimerFinished(){
        progress_countdown.progress = progress_countdown.getMax()
        textView_countdown.text = "SOS\nSent"
        Log.d("SOS","Send The SOS Now")
    }

    private fun startTimer(){
        timer = object : CountDownTimer(15000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished.toInt() / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun updateCountdownUI(){
        val secondsStr = (secondsRemaining+1).toString()
        textView_countdown.text = "${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        progress_countdown.progress = ((timerLengthSeconds - secondsRemaining)*10).toInt()
    }
}