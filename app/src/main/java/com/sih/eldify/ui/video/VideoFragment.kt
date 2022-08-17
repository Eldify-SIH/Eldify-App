package com.sih.eldify.ui.video

import android.app.Activity
import android.content.Context
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
import java.security.MessageDigest
import java.util.*


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

        val sharedPreferences = activity?.getSharedPreferences("basic_details", Context.MODE_PRIVATE)
        val uniqueString = sharedPreferences?.getString("USER_EMAIL", null) + sharedPreferences?.getString("EM_CONTACT_1", null)
        val hashString = md5(uniqueString)

            join_btn.setOnClickListener {
            if (hashString.isNotEmpty()) {
                val options = JitsiMeetConferenceOptions.Builder()
                    .setRoom(hashString)
                    .build()
                JitsiMeetActivity.launch(requireView().context, options)
            }
        }
    }

    fun md5(toEncrypt: String): String {
        return try {
            val digest: MessageDigest = MessageDigest.getInstance("md5")
            digest.update(toEncrypt.toByteArray())
            val bytes: ByteArray = digest.digest()
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(String.format("%02X", bytes[i]))
            }
            sb.toString().lowercase(Locale.getDefault())
        } catch (exc: Exception) {
            "" // Impossibru!
        }
    }
}