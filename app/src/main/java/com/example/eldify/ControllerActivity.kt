package com.example.eldify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.eldify.EchoWebSocketListener.Companion.NORMAL_CLOSURE_STATUS
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class ControllerActivity : AppCompatActivity() {
    private lateinit var IP_ADDR: String
    private lateinit var SOCKET_URL: String
    private var PORT = "80"


    private val client by lazy {
        OkHttpClient()
    }
    var wsTEMP: WebSocket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)
        IP_ADDR = intent.getStringExtra("IP_ADDR").toString()
        PORT = intent.getStringExtra("PORT").toString()
        SOCKET_URL = "ws://$IP_ADDR:$PORT"

        val command: TextView = findViewById(R.id.command)
        val on_button: Button =findViewById(R.id.onbutton)
        on_button.setOnClickListener {
            sendText("A")
            command.setText("ON")
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

        val requestMPU: Request = Request.Builder().url("$SOCKET_URL/COM").build()
        val listenerMPU =
            EchoWebSocketListener(this::outputCOM, this::ping, this::setConnectionStatus) {
                wsTEMP = null
            }
        wsTEMP = client.newWebSocket(requestMPU, listenerMPU)

    }

    private fun stop() {
        wsTEMP?.close(NORMAL_CLOSURE_STATUS, "Connection Closed!")
    }

    private fun ping(s: String) {
        runOnUiThread {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendText(command: String) {
        wsTEMP?.apply {
            send(command.toString())
//            Log.d("DEBUG", jsonObj.toString())
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