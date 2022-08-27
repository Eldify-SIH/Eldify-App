package com.sih.eldify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
    }
    fun loginButton(view: View){
        val intent = Intent(this, Login::class.java)
        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
