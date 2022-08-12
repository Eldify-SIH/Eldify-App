package com.example.eldify.websocket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.eldify.R

class ConnectionActivity : AppCompatActivity() {

    private val btnConnect: Button by lazy { findViewById(R.id.btn_connection_connect) }
    private val etIPAddr: EditText by lazy { findViewById(R.id.et_connection_ip_addr) }
    private val etPort: EditText by lazy { findViewById(R.id.et_connection_port) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)


        btnConnect.setOnClickListener {
            val IP = etIPAddr.text.toString()
            val port = etPort.text.toString()
            val intent = Intent(this, ControllerActivity::class.java)
            intent.putExtra("IP_ADDR", IP)
            intent.putExtra("PORT", port)
            startActivity(intent)
        }
    }
}