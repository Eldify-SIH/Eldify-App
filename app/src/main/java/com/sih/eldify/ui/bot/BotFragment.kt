package com.sih.eldify.ui.bot

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.sih.eldify.R
import kotlinx.android.synthetic.main.fragment_bot.*

class BotFragment : Fragment() {

    companion object {
        fun newInstance() = BotFragment()
    }

    private lateinit var viewModel: BotViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

}