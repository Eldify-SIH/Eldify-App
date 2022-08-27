package com.sih.eldify.ui.aizi

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sih.eldify.R
import com.sih.eldify.alz.dashboard.DashboardActivity
import com.sih.eldify.alz.dashboard.IntroSliderActivity

class AizeimerFragment : Fragment() {

    companion object {
        fun newInstance() = AizeimerFragment()
    }

    private lateinit var viewModel: AizeimerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val intent = Intent(activity, DashboardActivity::class.java)
        startActivity(intent)

        return inflater.inflate(R.layout.fragment_aizeimer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AizeimerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}