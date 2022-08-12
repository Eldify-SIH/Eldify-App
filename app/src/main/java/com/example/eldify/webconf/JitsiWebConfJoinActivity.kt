package com.example.eldify.webconf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.eldify.R
import kotlinx.android.synthetic.main.activity_jitsi_web_conf_join.*
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions

class JitsiWebConfJoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jitsi_web_conf_join)
    }

    fun onButtonClick(view: View) {
        val text=conferenceName.text.toString()
        if (text.isNotEmpty()){
            val options= JitsiMeetConferenceOptions.Builder()
                .setRoom(text)
                .build()
            JitsiMeetActivity.launch(this,options)
        }
    }
}
