package com.sih.eldify

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.sih.eldify.utils.SOS
import com.sih.eldify.websockets.EchoWebSocketListener
import kotlinx.android.synthetic.main.activity_basic_details.*
import kotlinx.android.synthetic.main.fragment_bot.*
import kotlinx.android.synthetic.main.fragment_sos.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject

private var SOCKET_URL_BOT: String? = null
private var IP_ADDR_1: String? = null
private var IP_ADDR_2: String? = null

private val client by lazy {
    OkHttpClient()
}

var wsSOS: WebSocket? = null

class BasicDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_details)

        val sharedPreferences = getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)
        if(sharedPreferences.getString("USER_NAME", null) != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        user_basic_details_submit.setOnClickListener {
            saveData()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveData() {
        val user_name = basic_user_name.text.toString()
        val user_age = basic_user_age.text.toString()
        val em_contact_1 = basic_em_contact_1.text.toString()
        val em_contact_2 = basic_em_contact_2.text.toString()
        val user_email = basic_user_email.text.toString()

        val sharedPreferences = getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)
        val sharedPreferencesIP = getSharedPreferences("IP_CONNECTION", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putString("USER_NAME", user_name)
            putString("USER_AGE", user_age)
            putString("EM_CONTACT_1", em_contact_1)
            putString("EM_CONTACT_2", em_contact_2)
            putString("USER_EMAIL", user_email)
        }.apply()

        Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()

        setIPAddress(sharedPreferencesIP)
        setURLBot()
    }

    private fun setIPAddress(sharedPreferences: SharedPreferences?) {
        if(sharedPreferences?.getString("IP_1", null) != null && sharedPreferences.getString("IP_2", null) != null) {
            IP_ADDR_1 = sharedPreferences.getString("IP_1", null)
            IP_ADDR_2 = sharedPreferences.getString("IP_2", null)
        }else{
            createIPSetBuilder(sharedPreferences)
        }
    }

    private fun createIPSetBuilder(sharedPreferences : SharedPreferences?){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Ip Reconnect")

        // set the custom layout

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
        runOnUiThread {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
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
        runOnUiThread {
//            sos_.setText(text)
            if(text.contains("recieved")){
                val gson = Gson()
                val sos: SOS = gson.fromJson(text, SOS::class.java)
                Log.d("tanvi","> From JSON String:\n" + sos)

                val sharedPreferencesBS = getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)
                val number_1 = sharedPreferencesBS?.getString("EM_CONTACT_1", null)
                val number_2 = sharedPreferencesBS?.getString("EM_CONTACT_2", null)

                sendSMS(number_1, sos.reason)
                sendSMS(number_2, sos.reason)

                callNumber(number_1)
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
            Toast.makeText(this, "Message Sent", Toast.LENGTH_LONG).show()

        } catch (e : Exception) {

            // on catch block we are displaying toast message for error.
            Toast.makeText(this, "Please enter all the data.."+e.message.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun setConnectionStatus(txt: String) {
        runOnUiThread {
//            if (txt == "Disconnected!") {
//                status.setTextColor(Color.RED)
//            } else {
//                status.setTextColor(Color.GREEN)
//            }
//            status.text = txt

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
}