package com.sih.eldify.ui.Meds

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sih.eldify.databinding.FragmentMedsBinding


class MedsFragment : Fragment() {

    private var _binding: FragmentMedsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val intent = Intent()
        intent.component = ComponentName("com.sih.eldify", "com.gautam.medicinetime.medicine.MedicineActivity")
        startActivity(intent)

        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}