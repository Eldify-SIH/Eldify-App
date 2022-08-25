package com.sih.eldify.ui.sos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class SosFragment : Fragment() {

    companion object {
        fun newInstance() = SosFragment()
    }

    private lateinit var viewModel: SosViewModel

    private var SOCKET_URL_BOT: String? = null
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
        return inflater.inflate(R.layout.fragment_sos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SosViewModel::class.java)
        // TODO: Use the ViewModel

        val sharedPreferences = activity?.getSharedPreferences("IP_CONNECTION", Context.MODE_PRIVATE)


        sos_reconnect.setOnClickListener {

            if(sharedPreferences?.getString("IP_1", null) != null && sharedPreferences.getString("IP_2", null) != null)
            {
                IP_ADDR_1 = sharedPreferences.getString("IP_1", null)
                IP_ADDR_2 = sharedPreferences.getString("IP_2", null)
            }
            else
            {
                createIPSetBuilder(sharedPreferences)
            }
        }

        sos_reconnect.setOnLongClickListener {

            createIPSetBuilder(sharedPreferences)

            true
        }

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
            .setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}