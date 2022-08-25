package com.sih.eldify.ui.bot

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sih.eldify.R
import com.sih.eldify.websockets.EchoWebSocketListener
import kotlinx.android.synthetic.main.fragment_bot.*
import kotlinx.android.synthetic.main.fragment_sos.*
import kotlinx.android.synthetic.main.ip_connection_layout.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject


class BotFragment : Fragment() {

    companion object {
        fun newInstance() = BotFragment()
    }

    private lateinit var viewModel: BotViewModel
    private var SOCKET_URL_BOT: String? = null
    private var URL_VIDEO: String? = null
    private var IP_ADDR_1: String? = null
    private var IP_ADDR_2: String? = null

    private val client by lazy {
        OkHttpClient()
    }

    var wsCOM: WebSocket? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_bot, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BotViewModel::class.java)
        // TODO: Use the ViewModel

        val sharedPreferences = activity?.getSharedPreferences("IP_CONNECTION", Context.MODE_PRIVATE)

        reconnect_bot.setOnClickListener {

            if(sharedPreferences?.getString("IP_1", null) != null && sharedPreferences.getString("IP_2", null) != null)
            {
                IP_ADDR_1 = sharedPreferences.getString("IP_1", null)
                IP_ADDR_2 = sharedPreferences.getString("IP_2", null)
            }
            else {
                createIPSetBuilder(sharedPreferences)
            }
        }

        reconnect_bot.setOnLongClickListener {

            createIPSetBuilder(sharedPreferences)

            true
        }

        if(IP_ADDR_1 != null && IP_ADDR_2 != null){

            // 192.168. + IP_ADDR_1 + . + IP_ADDR_2 + : + Port
            SOCKET_URL_BOT = "ws://192.168.$IP_ADDR_1.$IP_ADDR_2:80"
            URL_VIDEO = "ws://192.168.$IP_ADDR_1.$IP_ADDR_2:5000"
            Log.d("chk", SOCKET_URL_BOT!!)
            start()

            webView.loadUrl(URL_VIDEO!!)

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
            val requestCOM: Request = Request.Builder().url("$SOCKET_URL_BOT/COM").build()
            val listenerCOM =
                EchoWebSocketListener(this::outputCOM, this::ping, this::setConnectionStatus) {
                    wsCOM = null
                }
            wsCOM = client.newWebSocket(requestCOM, listenerCOM)
            Log.d("Debug", "Start executed")
        }
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
            if (txt == "Disconnected!") {
                status.setTextColor(Color.RED)
            } else {
                status.setTextColor(Color.GREEN)
            }
            status.text = txt

        }
    }

    fun createIPSetBuilder(sharedPreferences : SharedPreferences?){
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

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}