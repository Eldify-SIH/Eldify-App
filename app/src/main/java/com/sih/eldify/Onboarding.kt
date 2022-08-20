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
        val intent = Intent(this, MainActivity::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun getDataforOnboarding(): ArrayList<PaperOnboardingPage>? {

        // the first string is to show the main title ,
        // second is to show the message below the
        // title, then color of background is passed ,
        // then the image to show on the screen is passed
        // and at last icon to navigate from one screen to other
        val source = PaperOnboardingPage(
            "Gfg",
            "Welcome to GeeksForGeeks",
            Color.parseColor("#ffb174"),
            R.drawable.eldify,
            R.drawable.ic_bot_24
        )
        val source1 = PaperOnboardingPage(
            "Practice",
            "Practice questions from all topics",
            Color.parseColor("#22eaaa"),
            R.drawable.eldify,
            R.drawable.ic_bot_24
        )
        val source2 = PaperOnboardingPage(
            "",
            " ",
            Color.parseColor("#ee5a5a"),
            R.drawable.eldify,
            R.drawable.ic_bot_24
        )

        // array list is used to store
        // data of onbaording screen
        val elements: ArrayList<PaperOnboardingPage> = ArrayList()

        // all the sources(data to show on screens)
        // are added to array list
        elements.add(source)
        elements.add(source1)
        elements.add(source2)
        return elements
    }
}