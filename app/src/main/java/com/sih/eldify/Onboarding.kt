package com.sih.eldify


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage


class Onboarding : AppCompatActivity() {
    private lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        fragmentManager = supportFragmentManager

        // new instance is created and data is took from an
        // array list known as getDataonborading

        val paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataforOnboarding())
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // fragmentTransaction method is used
        // do all the transactions or changes
        // between different fragments
        fragmentTransaction.add(R.id.frame_layout, paperOnboardingFragment)
        // all the changes are committed
        fragmentTransaction.commit()

    }

        fun skipButton(view: View){
        val intent = Intent(this, BasicDetails::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun getDataforOnboarding(): ArrayList<PaperOnboardingPage>? {

        // the first string is to show the main title ,
        // second is to show the message below the
        // title, then color of background is passed ,
        // then the image to show on the screen is passed
        // and at last icon to navigate from one screen to other
        val source = PaperOnboardingPage(
            "Hey there !!",
            "I am Leo, your pet bot with futuristic features.",
            Color.parseColor("#82de94"),
            R.drawable.bot,
            R.drawable.ic_baseline_fiber_manual_record_24
        )
        val source1 = PaperOnboardingPage(
            "Bot Control",
            "I am your companion bot, take me wherever you go.",
            Color.parseColor("#a7e7d1"),
            R.drawable.controller,
            R.drawable.ic_baseline_fiber_manual_record_24
        )
        val source2 = PaperOnboardingPage(
            "SOS",
            "I will help in notifying your family in case of emergencies.",
            Color.parseColor("#7fbf7e"),
            R.drawable.fall,
            R.drawable.ic_baseline_fiber_manual_record_24
        )

        val source3 = PaperOnboardingPage(
            "Video Conferencing",
            "I'll assist you in connecting to your loved ones anytime, anywhere.",
            Color.parseColor("#82de94"),
            R.drawable.videocalling,
            R.drawable.ic_baseline_fiber_manual_record_24
        )

        val source4 = PaperOnboardingPage(
            "Voice Assistant",
            "Let me keep you updated with news, weather forecast and entertain you with jokes.",
            Color.parseColor("#a7e7d1"),
            R.drawable.microphone,
            R.drawable.ic_baseline_fiber_manual_record_24
        )

        // array list is used to store
        // data of onbaording screen
        val elements: ArrayList<PaperOnboardingPage> = ArrayList()

        // all the sources(data to show on screens)
        // are added to array list
        elements.add(source)
        elements.add(source1)
        elements.add(source2)
        elements.add(source3)
        elements.add(source4)
        return elements
    }
}