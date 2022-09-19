package com.sih.eldify.ui.Meds

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sih.eldify.alz.dashboard.DashboardActivity
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

        val mIntent = activity?.packageManager?.getLaunchIntentForPackage("com.sih.test")
        if (mIntent != null) {
            try {
                startActivity(mIntent)
            } catch (e : ActivityNotFoundException) {
                Toast.makeText(activity,
                "Err", Toast.LENGTH_SHORT).show();
            }
//
//
        }

//        val intent = Intent(Intent.ACTION_MAIN)
//        intent.component = ComponentName("com.gautam.medicinetime.mock", "com.gautam.medicinetime.medicine.MedicineActivity")
//        startActivity(intent)

//        startActivity(Intent(activity, MedicineActivity::class.java))

        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}