package com.sih.eldify.ui.yoga

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sih.eldify.MainActivity
import com.sih.eldify.alz.dashboard.DashboardActivity
import com.sih.eldify.databinding.FragmentGalleryBinding
import com.sih.eldify.yoga.YogaFirstActicity

class YogaFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val yogaViewModel =
            ViewModelProvider(this).get(YogaViewModel::class.java)

        val intent = Intent(activity, YogaFirstActicity::class.java)
        startActivity(intent)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}