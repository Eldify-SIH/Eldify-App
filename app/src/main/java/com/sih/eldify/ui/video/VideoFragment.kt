package com.sih.eldify.ui.video

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sih.eldify.R
import kotlinx.android.synthetic.main.fragment_video.*
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions


class VideoFragment : Fragment() {

    companion object {
        fun newInstance() = VideoFragment()
    }

    private lateinit var viewModel: VideoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VideoViewModel::class.java)
        // TODO: Use the ViewModel

        join_btn.setOnClickListener {
            val text = conferenceName.text.toString()
            if (text.isNotEmpty()) {
                val options = JitsiMeetConferenceOptions.Builder()
                    .setRoom(text)
                    .build()
                JitsiMeetActivity.launch(requireView().context, options)
            }
        }
    }
}