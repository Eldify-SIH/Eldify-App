package com.sih.eldify.medicines.medicine

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.sih.eldify.R
import com.sih.eldify.databinding.FragmentMedicineBinding
import com.sih.eldify.databinding.FragmentMedsBinding
import com.sih.eldify.databinding.FragmentSettingsBinding
import com.sih.eldify.medicines.addmedicine.AddMedicinesActivity
import com.sih.eldify.medicines.data.source.MedicineAlarm
import com.sih.eldify.ui.settings.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_medicine.*
import java.util.*

private var _binding: FragmentMedicineBinding? = null

private val binding get() = _binding!!

class MedicineFragment : Fragment(), MedicineContract.View,
    MedicineAdapter.OnItemClickListener {

    var unbinder: Unbinder? = null
    private var presenter: MedicineContract.Presenter? = null
    private var medicineAdapter: MedicineAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        medicineAdapter = MedicineAdapter(ArrayList<MedicineAlarm>())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMedicineBinding.inflate(inflater, container, false)
        val root: View = binding.root
        unbinder = ButterKnife.bind(this, root)
        setAdapter()
        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        medicineAdapter = MedicineAdapter(ArrayList<MedicineAlarm>())
        val fab = Objects.requireNonNull(
            requireActivity()
        ).findViewById<FloatingActionButton>(R.id.med_am_fab_add_task)
        fab.setImageResource(R.drawable.ic_add)
        fab.setOnClickListener { v: View? -> presenter!!.addNewMedicine() }
        med_fm_add_med_now.setOnClickListener{
            showAddMedicine()
        }
    }

    private fun setAdapter() {
        _binding!!.medFmMedicineList.adapter = medicineAdapter
        _binding!!.medFmMedicineList.layoutManager = LinearLayoutManager(context)
        _binding!!.medFmMedicineList.setHasFixedSize(true)
        medicineAdapter!!.setOnItemClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_WEEK]
        presenter!!.onStart(day)
    }

    override fun setPresenter(presenter: MedicineContract.Presenter?) {
        this.presenter = presenter
    }

    override fun showLoadingIndicator(active: Boolean) {
        if (view == null) {
            return
        }
        med_fm_progressLoader!!.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun showMedicineList(medicineAlarmList: List<MedicineAlarm?>?) {
        medicineAdapter!!.replaceData(medicineAlarmList as List<MedicineAlarm>?)
        med_fm_noMedText!!.visibility = View.VISIBLE
        med_fm_no_med_view!!.visibility = View.GONE
    }

    override fun showAddMedicine() {
        val intent = Intent(context, AddMedicinesActivity::class.java)
        startActivityForResult(intent, AddMedicinesActivity.addMedsActObj.REQUEST_ADD_TASK)
    }

    override fun showMedicineDetails(taskId: Long, medName: String?) {
        val intent = Intent(context, AddMedicinesActivity::class.java)
        intent.putExtra(AddMedicinesActivity.addMedsActObj.EXTRA_TASK_ID, taskId)
        intent.putExtra(AddMedicinesActivity.addMedsActObj.EXTRA_TASK_NAME, medName)
        startActivity(intent)
    }

    override fun showLoadingMedicineError() {
        showMessage(getString(R.string.loading_tasks_error))
    }

    override fun showNoMedicine() {
        showNoTasksViews(
            resources.getString(R.string.no_medicine_added)
        )
    }

    override fun showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_me_message))
    }

    override fun showMedicineDeletedSuccessfully() {
        showMessage(getString(R.string.successfully_deleted_message))
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_WEEK]
        presenter!!.onStart(day)
    }

    private fun showMessage(message: String) {
        if (view != null) Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override val isActive: Boolean
        get() = isAdded

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder!!.unbind()
    }

    private fun showNoTasksViews(mainText: String) {
        med_fm_medicine_list!!.visibility = View.GONE
        med_fm_no_med_view!!.visibility = View.VISIBLE
        med_fm_noMedText!!.setText(mainText)
        med_fm_noMedIcon!!.setImageDrawable(resources.getDrawable(R.drawable.icon_my_health))
        med_fm_add_med_now!!.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter!!.result(requestCode, resultCode)
    }

    override fun onMedicineDeleteClicked(medicineAlarm: MedicineAlarm?) {
        presenter!!.deleteMedicineAlarm(medicineAlarm, activity)
    }

    companion object {
        fun newInstance(): MedicineFragment {
            val args = Bundle()
            val fragment = MedicineFragment()
            fragment.arguments = args
            return fragment
        }
    }
}