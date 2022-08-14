package com.sih.eldify.ui.bot

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sih.eldify.R

class BotFragment : Fragment() {

    companion object {
        fun newInstance() = BotFragment()
    }

    private lateinit var viewModel: BotViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bot, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BotViewModel::class.java)
        // TODO: Use the ViewModel
    }

}