package com.sih.eldify

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_basic_details.*
import kotlinx.android.synthetic.main.fragment_settings.*
import java.security.MessageDigest
import java.util.*

class BasicDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_details)

        val sharedPreferences = getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)
        if(sharedPreferences.getString("USER_NAME", null) != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        user_basic_details_submit.setOnClickListener {
            Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show()
            saveData()
        }
    }

    private fun saveData() {
        val user_name = basic_user_name.text.toString()
        val user_age = basic_user_age.text.toString()
        val em_contact_1 = basic_em_contact_1.text.toString()
        val em_contact_2 = basic_em_contact_2.text.toString()
        val user_email = basic_user_email.text.toString()

        val sharedPreferences = getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putString("USER_NAME", user_name)
            putString("USER_AGE", user_age)
            putString("EM_CONTACT_1", em_contact_1)
            putString("EM_CONTACT_2", em_contact_2)
            putString("USER_EMAIL", user_email)
        }.apply()

        Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()
    }
}