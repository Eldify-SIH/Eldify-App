package com.sih.eldify


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        var sharedPreferences = getSharedPreferences("key", MODE_PRIVATE)
        var editor = sharedPreferences.edit()



        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({

            val intent = if(!sharedPreferences.getBoolean("first_launch", false)){
                editor.putBoolean("first_launch",false) //TODO: true later
                editor.apply()
                Intent(this,Onboarding::class.java)
            }else{
                Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}