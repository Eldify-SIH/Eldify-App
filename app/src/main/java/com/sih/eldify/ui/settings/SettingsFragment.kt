package com.sih.eldify.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sih.eldify.MainActivity
import com.sih.eldify.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.activity_basic_details.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        val sharedPreferences = activity?.getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)

        user_name.setText(sharedPreferences!!.getString("USER_EMAIL", null))
        user_age.setText(sharedPreferences!!.getString("USER_AGE", null))
        em_contact_1.setText(sharedPreferences!!.getString("EM_CONTACT_1", null))
        em_contact_2.setText(sharedPreferences!!.getString("EM_CONTACT_2", null))
        user_email.setText(sharedPreferences!!.getString("USER_EMAIL", null))

        user_details_submit.setOnClickListener {
            saveData()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        super.onActivityCreated(savedInstanceState)
    }

    private fun saveData() {
        val user_name = user_name.text.toString()
        val user_age = user_age.text.toString()
        val em_contact_1 = em_contact_1.text.toString()
        val em_contact_2 = em_contact_2.text.toString()
        val user_email = user_email.text.toString()

        val sharedPreferences = activity?.getSharedPreferences("BASIC_DETAILS", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.apply {
            putString("USER_NAME", user_name)
            putString("USER_AGE", user_age)
            putString("EM_CONTACT_1", em_contact_1)
            putString("EM_CONTACT_2", em_contact_2)
            putString("USER_EMAIL", user_email)
        }.apply()

        Toast.makeText(context, "Data added", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}