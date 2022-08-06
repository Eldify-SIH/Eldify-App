package com.example.eldify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.eldify.EchoWebSocketListener.Companion.NORMAL_CLOSURE_STATUS
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject

class ControllerActivity : AppCompatActivity() {
    private lateinit var IP_ADDR: String
    private lateinit var SOCKET_URL: String
    private var PORT = "80"


    private val commandtv: TextView by lazy { findViewById(R.id.command) }
    private val on_button: Button by lazy {findViewById(R.id.onbutton)}

    private val client by lazy {
        OkHttpClient()
    }
    var wsCOM: WebSocket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)
        IP_ADDR = intent.getStringExtra("IP_ADDR").toString()
        PORT = intent.getStringExtra("PORT").toString()
        SOCKET_URL = "ws://$IP_ADDR:$PORT"

        on_button.setOnClickListener {
            sendJSONOnCOM("A")
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

        val requestCOM: Request = Request.Builder().url("$SOCKET_URL/COM").build()
        val listenerCOM =
            EchoWebSocketListener(this::outputCOM, this::ping, this::setConnectionStatus) {
                wsCOM = null
            }
        wsCOM = client.newWebSocket(requestCOM, listenerCOM)
        Log.d("Debug","Start executed")
    }

    private fun stop() {
        wsCOM?.close(NORMAL_CLOSURE_STATUS, "Connection Closed!")
    }

    private fun ping(s: String) {
        runOnUiThread {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendJSONOnCOM(command: String) {
        wsCOM?.apply {
            var jsonObj = JSONObject()
            jsonObj.put("COM", command)
            send(jsonObj.toString())
//            Log.d("DEBUG", jsonObj.toString())
            commandtv.text = command
        } ?: ping("Error: Restart the App to reconnect")
    }

    private fun outputCOM(txt: String) {
        runOnUiThread {

        }
    }

    fun setConnectionStatus(txt: String) {
        runOnUiThread {

        }
    }

}