package com.sih.eldify.medicines.medicine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.sih.eldify.medicines.R
import com.sih.eldify.medicines.addmedicine.AddMedicineActivity
import com.sih.eldify.medicines.data.source.MedicineAlarm
import com.sih.eldify.medicines.views.RobotoLightTextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.*

package com.sih.eldify.medicines.medicine
import com.sih.eldify.medicines.R
import com.sih.eldify.medicines.addmedicine.AddMedicineActivity
import com.sih.eldify.medicines.data.source.MedicineAlarm
import com.sih.eldify.medicines.views.RobotoLightTextView

/**
 * Created by gautam on 13/07/17.
 */
class MedicineFragment : Fragment(), MedicineContract.View,
    MedicineAdapter.OnItemClickListener {
    @BindView(R.id.medicine_list)
    var rvMedList: RecyclerView? = null
    var unbinder: Unbinder? = null

    @BindView(R.id.noMedIcon)
    var noMedIcon: ImageView? = null

    @BindView(R.id.noMedText)
    var noMedText: RobotoLightTextView? = null

    @BindView(R.id.add_med_now)
    var addMedNow: TextView? = null

    @BindView(R.id.no_med_view)
    var noMedView: View? = null

    @BindView(R.id.progressLoader)
    var progressLoader: ProgressBar? = null
    private var presenter: MedicineContract.Presenter? = null
    private var medicineAdapter: MedicineAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        medicineAdapter = MedicineAdapter(ArrayList<E>(0))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_medicine, container, false)
        unbinder = ButterKnife.bind(this, view)
        setAdapter()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fab = Objects.requireNonNull(
            activity
        ).findViewById<FloatingActionButton>(R.id.fab_add_task)
        fab.setImageResource(R.drawable.ic_add)
        fab.setOnClickListener { v: View? -> presenter!!.addNewMedicine() }
    }

    private fun setAdapter() {
        rvMedList!!.adapter = medicineAdapter
        rvMedList!!.layoutManager = LinearLayoutManager(context)
        rvMedList!!.setHasFixedSize(true)
        medicineAdapter!!.setOnItemClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_WEEK]
        presenter!!.onStart(day)
    }

    fun setPresenter(presenter: MedicineContract.Presenter?) {
        this.presenter = presenter
    }

    override fun showLoadingIndicator(active: Boolean) {
        if (view == null) {
            return
        }
        progressLoader!!.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun showMedicineList(medicineAlarmList: List<MedicineAlarm?>?) {
        medicineAdapter!!.replaceData(medicineAlarmList)
        rvMedList!!.visibility = View.VISIBLE
        noMedView!!.visibility = View.GONE
    }

    override fun showAddMedicine() {
        val intent = Intent(context, AddMedicineActivity::class.java)
        startActivityForResult(intent, AddMedicineActivity.REQUEST_ADD_TASK)
    }

    override fun showMedicineDetails(taskId: Long, medName: String?) {
        val intent = Intent(context, AddMedicineActivity::class.java)
        intent.putExtra(AddMedicineActivity.EXTRA_TASK_ID, taskId)
        intent.putExtra(AddMedicineActivity.EXTRA_TASK_NAME, medName)
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
        if (view != null) Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }

    override val isActive: Boolean
        get() = isAdded

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder!!.unbind()
    }

    @OnClick(R.id.add_med_now)
    fun addMedicine() {
        showAddMedicine()
    }

    private fun showNoTasksViews(mainText: String) {
        rvMedList!!.visibility = View.GONE
        noMedView!!.visibility = View.VISIBLE
        noMedText.setText(mainText)
        noMedIcon!!.setImageDrawable(resources.getDrawable(R.drawable.icon_my_health))
        addMedNow!!.visibility = View.VISIBLE
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