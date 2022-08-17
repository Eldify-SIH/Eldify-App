package com.sih.eldify

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_basic_details.*

class BasicDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_details)

        user_basic_details_submit.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val user_name = basic_user_name.text.toString()
        val user_age = basic_user_age.text.toString()
        val em_contact_1 = basic_em_contact_1.text.toString()
        val em_contact_2 = basic_em_contact_2.text.toString()
        val user_email = basic_user_email.text.toString()

        val sharedPreferences = getSharedPreferences("basic_details", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("USER_NAME", user_name)
            putString("USER_AGE", user_age)
            putString("EM_CONTACT_1", em_contact_1)
            putString("EM_CONTACT_2", em_contact_2)
            putString("USER_EMAIL", user_email)
        }
    }
}