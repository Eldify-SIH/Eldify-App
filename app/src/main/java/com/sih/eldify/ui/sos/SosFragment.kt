package com.sih.eldify.ui.sos

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sih.eldify.R

class SosFragment : Fragment() {

    companion object {
        fun newInstance() = SosFragment()
    }

    private lateinit var viewModel: SosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SosViewModel::class.java)
        // TODO: Use the ViewModel
    }

}