package com.sih.eldify.mediciness.addmedicine

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import butterknife.*
import com.sih.eldify.medicines.alarm.ReminderActivity
import com.sih.eldify.medicines.alarm.ReminderFragment
import com.sih.eldify.medicines.data.source.MedicineAlarm
import com.sih.eldify.medicines.data.source.Pills
import com.sih.eldify.medicines.views.DayViewCheckBox
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import com.sih.eldify.R
import kotlinx.android.synthetic.main.fragment_add_medicine.*
import kotlinx.android.synthetic.main.fragment_add_medicine.view.*


/**
 * Created by gautam on 12/07/17.
 */
class AddMedicineFragment : Fragment(),
    AddMedicineContract.View {


    private var doseUnitList: List<String>? = null
    private val dayOfWeekList = BooleanArray(7)
    private var hour = 0
    private var minute = 0
    var unbinder: Unbinder? = null
    private var mPresenter: AddMedicineContract.Presenter? = null
    private var rootView: View? = null
    private var doseUnit: String? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fab = Objects.requireNonNull(
            requireActivity()
        ).findViewById<FloatingActionButton>(R.id.med_aam_fab_edit_task_done)
        fab.setImageResource(R.drawable.ic_done)
        fab.setOnClickListener(setClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_add_medicine, container, false)
        unbinder = ButterKnife.bind(this, rootView!!)

        rootView!!.med_fam_every_day.setOnClickListener{
            onCheckboxClicked(rootView!!)
        }
        rootView!!.med_fam_dv_monday.setOnClickListener{
            onCheckboxClicked(rootView!!)
        }
        rootView!!.med_fam_dv_tuesday.setOnClickListener{
            onCheckboxClicked(rootView!!)
        }
        rootView!!.med_fam_dv_wednesday.setOnClickListener{
            onCheckboxClicked(rootView!!)
        }
        rootView!!.med_fam_dv_thursday.setOnClickListener{
            onCheckboxClicked(rootView!!)
        }
        rootView!!.med_fam_dv_friday.setOnClickListener{
            onCheckboxClicked(rootView!!)
        }
        rootView!!.med_fam_dv_saturday.setOnClickListener{
            onCheckboxClicked(rootView!!)
        }
        rootView!!.med_fam_dv_sunday.setOnClickListener{
            onCheckboxClicked(rootView!!)
        }
        rootView!!.med_fam_tv_medicine_time.setOnClickListener{
            showTimePicker()
        }

        rootView!!.med_fam_spinner_dose_units.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onSpinnerItemSelected(position)
            }

        }
        setCurrentTime()
//        setmed_fam_spinner_dose_units()
        return rootView
    }

    override fun setPresenter(presenter: AddMedicineContract.Presenter?) {
        mPresenter = presenter
    }

    override fun showEmptyMedicineError() {
        // Snackbar.make(mTitle, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
    }

    override fun showMedicineList() {
        Objects.requireNonNull(requireActivity()).setResult(Activity.RESULT_OK)
        requireActivity().finish()
    }

    override val isActive: Boolean
        get() = isAdded

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder!!.unbind()
    }

    fun onCheckboxClicked(view: View) {
        val checked = med_fam_every_day.isChecked
        when (view.getId()) {
            R.id.med_fam_dv_monday -> if (checked) {
                dayOfWeekList[1] = true
            } else {
                dayOfWeekList[1] = false
                med_fam_every_day!!.isChecked = false
            }
            R.id.med_fam_dv_tuesday -> if (checked) {
                dayOfWeekList[2] = true
            } else {
                dayOfWeekList[2] = false
                med_fam_every_day!!.isChecked = false
            }
            R.id.med_fam_dv_wednesday -> if (checked) {
                dayOfWeekList[3] = true
            } else {
                dayOfWeekList[3] = false
                med_fam_every_day!!.isChecked = false
            }
            R.id.med_fam_dv_thursday -> if (checked) {
                dayOfWeekList[4] = true
            } else {
                dayOfWeekList[4] = false
                med_fam_every_day!!.isChecked = false
            }
            R.id.med_fam_dv_friday -> if (checked) {
                dayOfWeekList[5] = true
            } else {
                dayOfWeekList[5] = false
                med_fam_every_day!!.isChecked = false
            }
            R.id.med_fam_dv_saturday -> if (checked) {
                dayOfWeekList[6] = true
            } else {
                dayOfWeekList[6] = false
                med_fam_every_day!!.isChecked = false
            }
            R.id.med_fam_dv_sunday -> if (checked) {
                dayOfWeekList[0] = true
            } else {
                dayOfWeekList[0] = false
                med_fam_every_day!!.isChecked = false
            }
            R.id.med_fam_every_day -> {
                val ll = rootView!!.findViewById<View>(R.id.med_fam_checkbox_layout) as LinearLayout
                var i = 0
                while (i < ll.childCount) {
                    val v = ll.getChildAt(i)
                    (v as DayViewCheckBox).setChecked(checked)
                    onCheckboxClicked(v)
                    i++
                }
            }
        }
    }

    private fun showTimePicker() {
        val mCurrentTime = Calendar.getInstance()
        hour = mCurrentTime[Calendar.HOUR_OF_DAY]
        minute = mCurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(context,
            { timePicker, selectedHour, selectedMinute ->
                hour = selectedHour
                minute = selectedMinute
                med_fam_tv_medicine_time?.setText(
                    String.format(
                        Locale.getDefault(),
                        "%d:%d",
                        selectedHour,
                        selectedMinute
                    )
                )
            }, hour, minute, false
        ) //No 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    private fun setCurrentTime() {
        val mCurrentTime = Calendar.getInstance()
        hour = mCurrentTime[Calendar.HOUR_OF_DAY]
        minute = mCurrentTime[Calendar.MINUTE]
        med_fam_tv_medicine_time?.setText(String.format(Locale.getDefault(), "%d:%d", hour, minute))
    }

    private fun setmed_fam_spinner_dose_units(rootView: View) {
        doseUnitList = Arrays.asList(*resources.getStringArray(R.array.medications_shape_array))
        val adapter = ArrayAdapter(
            Objects.requireNonNull(
                requireActivity()
            ), android.R.layout.simple_dropdown_item_1line, doseUnitList as MutableList<String>
        )
        med_fam_spinner_dose_units!!.adapter = adapter
    }

    fun onSpinnerItemSelected(position: Int) {
        if (doseUnitList == null || doseUnitList!!.isEmpty()) {
            return
        }
        doseUnit = doseUnitList!![position]
        Log.d("TAG", doseUnit!!)
    }

    private val setClickListener = View.OnClickListener {
        var checkBoxCounter = 0
        val pill_name = med_fam_edit_med_name!!.text.toString()
        val doseQuantity = med_fam_tv_dose_quantity!!.text.toString()
        val takeTime = Calendar.getInstance()
        val date = takeTime.time
        val dateString = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)

        /** Updating model  */
        /** Updating model  */
        val alarm = MedicineAlarm()
        val alarmId = Random().nextInt(100)
        /** If Pill does not already exist  */
        /** If Pill does not already exist  */
        if (!mPresenter?.isMedicineExits(pill_name)!!) {
            val pill = Pills()
            pill.pillName = pill_name
            alarm.dateString = dateString
            alarm.hour = hour
            alarm.minute = minute
            alarm.pillName = pill_name
            alarm.dayOfWeek = dayOfWeekList
            alarm.doseUnit = doseUnit
            alarm.doseQuantity = doseQuantity
            alarm.alarmId = alarmId
            pill.addAlarm(alarm)
            val pillId: Long = mPresenter!!.addPills(pill)
            pill.pillId = pillId
            mPresenter!!.saveMedicine(alarm, pill)
        } else { // If Pill already exists
            val pill: Pills? = mPresenter!!.getPillsByName(pill_name)
            alarm.dateString = dateString
            alarm.hour = hour
            alarm.minute = minute
            alarm.pillName = pill_name
            alarm.dayOfWeek = dayOfWeekList
            alarm.doseUnit = doseUnit
            alarm.doseQuantity = doseQuantity
            alarm.alarmId = alarmId
            pill!!.addAlarm(alarm)
            mPresenter!!.saveMedicine(alarm, pill)
        }
        var ids: List<Long> = LinkedList()
        try {
            val alarms: List<MedicineAlarm?>? = mPresenter!!.getMedicineByPillName(pill_name)
            for (tempAlarm in alarms!!) {
                if (tempAlarm!!.hour === hour && tempAlarm!!.minute === minute) {
                    ids = tempAlarm!!.getIds()
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        for (i in 0..6) {
            if (dayOfWeekList[i] && pill_name.length != 0) {
                val dayOfWeek = i + 1
                val _id = ids[checkBoxCounter]
                val id = _id.toInt()
                checkBoxCounter++
                /** This intent invokes the activity ReminderActivity, which in turn opens the AlertAlarm window  */
                /** This intent invokes the activity ReminderActivity, which in turn opens the AlertAlarm window  */
                val intent = Intent(activity, ReminderActivity::class.java)
                intent.putExtra(ReminderFragment.EXTRA_ID, _id)
                val operation = PendingIntent.getActivity(
                    activity,
                    id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                /** Getting a reference to the System Service ALARM_SERVICE  */
                /** Getting a reference to the System Service ALARM_SERVICE  */
                val alarmManager = Objects.requireNonNull(
                    requireActivity()
                ).getSystemService(Context.ALARM_SERVICE) as AlarmManager

                /** Creating a calendar object corresponding to the date and time set by the user  */
                /** Creating a calendar object corresponding to the date and time set by the user  */
                val calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = hour
                calendar[Calendar.MINUTE] = minute
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0
                calendar[Calendar.DAY_OF_WEEK] = dayOfWeek
                /** Converting the date and time in to milliseconds elapsed since epoch  */
                /** Converting the date and time in to milliseconds elapsed since epoch  */
                var alarm_time = calendar.timeInMillis
                if (calendar.before(Calendar.getInstance())) alarm_time += AlarmManager.INTERVAL_DAY * 7
                assert(alarmManager != null)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP, alarm_time,
                    AlarmManager.INTERVAL_DAY * 7, operation
                )
            }
        }
        Toast.makeText(context, "Alarm for $pill_name is set successfully", Toast.LENGTH_SHORT)
            .show()
        showMedicineList()
    }

    companion object {
        const val ARGUMENT_EDIT_MEDICINE_ID = "ARGUMENT_EDIT_MEDICINE_ID"
        const val ARGUMENT_EDIT_MEDICINE_NAME = "ARGUMENT_EDIT_MEDICINE_NAME"
        fun newInstance(): AddMedicineFragment {
            val args = Bundle()
            val fragment = AddMedicineFragment()
            fragment.arguments = args
            return fragment
        }
    }
}


