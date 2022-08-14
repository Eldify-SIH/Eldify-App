package com.example.eldify.ui.sos

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.eldify.R
import com.example.eldify.databinding.FragmentSOBinding

class SOSFragment : Fragment() {

    private var _binding: FragmentSOBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sosViewModel =
            ViewModelProvider(this).get(SOSViewModel::class.java)

        _binding = FragmentSOBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSos
        sosViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}