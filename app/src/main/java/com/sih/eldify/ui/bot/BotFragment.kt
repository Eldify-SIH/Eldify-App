package com.sih.eldify.ui.bot

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import com.sih.eldify.R
import kotlinx.android.synthetic.main.fragment_bot.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject

class BotFragment : Fragment() {

    companion object {
        fun newInstance() = BotFragment()
    }

    private lateinit var viewModel: BotViewModel
    private lateinit var SOCKET_URL: String
    private var IP_ADDR: String? = null
    private var port: String? = null

    private val client by lazy {
        OkHttpClient()
    }
    var wsCOM: WebSocket? = null

    private var PORT = "80"


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        btn_connection_connect.setOnClickListener {
            IP_ADDR = et_connection_ip_addr.text.toString()
            port = et_connection_port.text.toString()
        }

        if(IP_ADDR != null && port != null){

            SOCKET_URL = "ws://$IP_ADDR:$PORT"

            btn_controller_forward.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    Log.d("Debug", "Forward")
                    sendJSONOnCOM("F", "Forward")
                } else if (event.action == MotionEvent.ACTION_UP) {
                    Log.d("Debug", "Forward Released")
                    sendJSONOnCOM("S", "S")
                }
                false
            }

            btn_controller_backward.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    Log.d("Debug", "Backward")
                    sendJSONOnCOM("B", "Backward")
                } else if (event.action == MotionEvent.ACTION_UP) {
                    Log.d("Debug", "Backward Released")
                    sendJSONOnCOM("S", "S")
                }
                false
            }

            btn_controller_left.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    Log.d("Debug", "Left")
                    sendJSONOnCOM("L", "Left")
                } else if (event.action == MotionEvent.ACTION_UP) {
                    Log.d("Debug", "Left Released")
                    sendJSONOnCOM("S", "S")
                }
                false
            }

            btn_controller_right.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    Log.d("Debug", "Right")
                    sendJSONOnCOM("R", "Right")
                } else if (event.action == MotionEvent.ACTION_UP) {
                    Log.d("Debug", "Right Released")
                    sendJSONOnCOM("S", "S")
                }
                false
            }
        }
        return inflater.inflate(R.layout.fragment_bot, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BotViewModel::class.java)
        // TODO: Use the ViewModel
        webView.loadUrl("http://192.168.199.189:5000")

        val webSettings : WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        webView.canGoBack()
        webView.setOnKeyListener { v, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_BACK
                && event.action == MotionEvent.ACTION_UP
                && webView.canGoBack()){
                webView.goBack()
                return@setOnKeyListener true
            }
            false
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
        Log.d("Debug", "Start executed")
    }

    private fun stop() {
        wsCOM?.close(EchoWebSocketListener.NORMAL_CLOSURE_STATUS, "Connection Closed!")
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

    private fun outputCOM(txt: String) {
        activity?.runOnUiThread {

        }
    }

    fun setConnectionStatus(txt: String) {
        activity?.runOnUiThread {

        }
    }

}