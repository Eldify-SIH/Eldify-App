package com.sih.eldify.ui.Meds

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sih.eldify.databinding.FragmentMedsBinding
import com.sih.eldify.medicines.medicine.MedicineActivity


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

        val intent = Intent(activity, MedicineActivity::class.java)
        startActivity(intent)

        val medsViewModel =
            ViewModelProvider(this).get(MedsViewModel::class.java)

        _binding = FragmentMedsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        medsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}