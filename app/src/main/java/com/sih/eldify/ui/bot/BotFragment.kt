package com.sih.eldify.ui.bot

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sih.eldify.R
import com.sih.eldify.websockets.EchoWebSocketListener
import kotlinx.android.synthetic.main.fragment_bot.*
import kotlinx.android.synthetic.main.fragment_sos.*
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
    private var VID_IP_ADDR_1: String? = null
    private var VID_IP_ADDR_2: String? = null

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

        setIPAddress(sharedPreferences)

        reconnect_bot.setOnClickListener {
            setIPAddress(sharedPreferences)
            stop()
            start()
        }

        reconnect_bot.setOnLongClickListener {

            createIPSetBuilder(sharedPreferences)
            stop()
            start()

            true
        }

        if(IP_ADDR_1 != null && IP_ADDR_2 != null && VID_IP_ADDR_1 != null && VID_IP_ADDR_2 != null){

            setURLBotNVid()
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

        hfSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                sendJSONOnCOM("C", "C")
                sendJSONOnCOM("S", "C")
            }else{
                sendJSONOnCOM("D", "D")
                sendJSONOnCOM("S", "D")
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

    private fun setIPAddress(sharedPreferences: SharedPreferences?) {
        if(sharedPreferences?.getString("IP_1", null) != null && sharedPreferences.getString("IP_2", null) != null && sharedPreferences.getString("VID_IP_1", null) != null && sharedPreferences.getString("VID_IP_2", null) != null) {
            IP_ADDR_1 = sharedPreferences?.getString("IP_1", null)
            IP_ADDR_2 = sharedPreferences?.getString("IP_2", null)
            VID_IP_ADDR_1 = sharedPreferences.getString("VID_IP_1", null)
            VID_IP_ADDR_2 = sharedPreferences.getString("VID_IP_2", null)
        }else{
            createIPSetBuilder(sharedPreferences)
        }
    }

    private fun setURLBotNVid() {
        // 192.168. + IP_ADDR_1 + . + IP_ADDR_2 + : + Port
        SOCKET_URL_BOT = "ws://192.168.$IP_ADDR_1.$IP_ADDR_2:80"
        URL_VIDEO = "http://192.168.$VID_IP_ADDR_1.$VID_IP_ADDR_2:5000"
        Log.d("chk", SOCKET_URL_BOT!!)
        start()

        webView.loadUrl(URL_VIDEO!!)

        val webSettings : WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        var loadingFinished = true
        var redirect = false

//        webView.visibility = View.VISIBLE
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, urlNewString: String): Boolean {
                if (!loadingFinished) {
                    redirect = true
                }
                loadingFinished = false
                view.loadUrl(urlNewString)
                return true
            }

//            override fun onPageStarted(view: WebView, url: String, facIcon: Bitmap) {
//                loadingFinished = false
//                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
//                view.visibility = View.VISIBLE
//            }

            override fun onPageFinished(view: WebView, url: String) {
                if (!redirect) {
                    loadingFinished = true
                }
                if (loadingFinished && !redirect) {
                    //HIDE LOADING IT HAS FINISHED
                    view.visibility = View.VISIBLE
                } else {
                    redirect = false
                }
            }
        })

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
        stop()
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

    fun setConnectionStatus(txt: String) {
        activity?.runOnUiThread {
            if (txt == "Disconnected!") {
                status.setTextColor(Color.RED)
                color_controller.setCardBackgroundColor(Color.RED)
            } else {
                status.setTextColor(Color.GREEN)
                color_controller.setCardBackgroundColor(Color.RED)
            }
            status.text = txt
//            color_controller.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(Color.RED)))
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
        val vid_ip_1: EditText = ipLayout.findViewById(R.id.et_connection_ip_addr_1_vid)
        val vid_ip_2: EditText = ipLayout.findViewById(R.id.et_connection_ip_addr_2_vid)
        val btn : Button = ipLayout.findViewById(R.id.btn_connect_ip)

        if(sharedPreferences?.getString("IP_1", null) != null){
            ip_1.setText(sharedPreferences.getString("IP_1", null).toString())
        }
        if(sharedPreferences?.getString("IP_2", null) != null){
            ip_2.setText(sharedPreferences.getString("IP_2", null).toString())
        }
        if(sharedPreferences?.getString("VID_IP_1", null) != null){
            vid_ip_1.setText(sharedPreferences.getString("VID_IP_1", null).toString())
        }
        if(sharedPreferences?.getString("VID_IP_2", null) != null) {
            vid_ip_2.setText(sharedPreferences.getString("VID_IP_2", null).toString())
        }

        val dialog: AlertDialog = builder.create()

        btn.setOnClickListener {
            IP_ADDR_1 = ip_1.text.toString()
            IP_ADDR_2 = ip_2.text.toString()

            VID_IP_ADDR_1 = vid_ip_1.text.toString()
            VID_IP_ADDR_2 = vid_ip_2.text.toString()

            val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.apply {
                putString("IP_1", IP_ADDR_1)
                putString("IP_2", IP_ADDR_2)
                putString("VID_IP_1", VID_IP_ADDR_1)
                putString("VID_IP_2", VID_IP_ADDR_2)
            }.apply()

            dialog.dismiss()
        }


        dialog.show()
    }



}