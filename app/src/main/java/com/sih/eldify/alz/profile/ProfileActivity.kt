package com.sih.eldify.alz.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.sih.eldify.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val prevIntent = intent
        val playAnim = prevIntent.getIntExtra("play_anim", 1)
        if (playAnim == 1) overridePendingTransition(R.anim.right_to_left_slide_in, R.anim.right_to_left_slide_out)
    }

    fun onEditDetailsClick(view: View?) {
        startActivity(Intent(applicationContext, DetailsActivity::class.java))
    }

    fun onBackPressed(view: View?) {
        finish()
        overridePendingTransition(R.anim.left_to_right_slide_in, R.anim.left_to_right_slide_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.left_to_right_slide_in, R.anim.left_to_right_slide_out)
    }
}