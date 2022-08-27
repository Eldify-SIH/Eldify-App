package com.sih.eldify.medicines.medicine

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.sih.eldify.medicines.addmedicine.AddMedicinesActivity
import com.sih.eldify.medicines.alarm.ReminderActivity
import com.sih.eldify.medicines.alarm.ReminderFragment
import com.sih.eldify.medicines.data.source.MedicineAlarm
import com.sih.eldify.medicines.data.source.MedicineDataSource
import com.sih.eldify.medicines.data.source.MedicineRepository
import java.util.*


/**
 * Created by gautam on 13/07/17.
 */
class MedicinePresenter internal constructor(
    medicineRepository: MedicineRepository,
    medView: MedicineContract.View
) :
    MedicineContract.Presenter {
    private val mMedicineRepository: MedicineRepository
    private val mMedView: MedicineContract.View
    override fun loadMedicinesByDay(day: Int, showIndicator: Boolean) {
        loadListByDay(day, showIndicator)
    }

    override fun deleteMedicineAlarm(medicineAlarm: MedicineAlarm?, context: Context?) {
        val alarms: List<MedicineAlarm?>? =
            mMedicineRepository.getAllAlarms(medicineAlarm!!.pillName)
        for (alarm in alarms!!) {
            mMedicineRepository.deleteAlarm(alarm!!.id)
            /** This intent invokes the activity ReminderActivity, which in turn opens the AlertAlarm window  */
            val intent = Intent(context, ReminderActivity::class.java)
            intent.putExtra(ReminderFragment.EXTRA_ID, alarm.alarmId)
            val operation = PendingIntent.getActivity(
                context,
                alarm.alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            /** Getting a reference to the System Service ALARM_SERVICE  */
            val alarmManager = Objects.requireNonNull(context)
                ?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager?.cancel(operation)
        }
        mMedView.showMedicineDeletedSuccessfully()
    }

    override fun start() {}

    override fun onStart(day: Int) {
        Log.d("TAG", "onStart: $day")
        loadMedicinesByDay(day, true)
    }

    override fun reload(day: Int) {
        Log.d("TAG", "reload: $day")
        loadListByDay(day, true)
    }

    override fun result(requestCode: Int, resultCode: Int) {
        if (AddMedicinesActivity.addMedsActObj.REQUEST_ADD_TASK === requestCode && Activity.RESULT_OK == resultCode) {
            mMedView.showSuccessfullySavedMessage()
        }
    }

    override fun addNewMedicine() {
        mMedView.showAddMedicine()
    }

    private fun loadListByDay(day: Int, showLoadingUi: Boolean) {
        if (showLoadingUi) mMedView.showLoadingIndicator(true)
        mMedicineRepository.getMedicineListByDay(day, object : MedicineDataSource.LoadMedicineCallbacks {
            override fun onMedicineLoaded(medicineAlarmList: List<MedicineAlarm>?) {

                Log.d("med", medicineAlarmList.toString())

                processMedicineList(medicineAlarmList)
                // The view may not be able to handle UI updates anymore
                if (!mMedView.isActive) {
                    return
                }
                if (showLoadingUi) {
                    mMedView.showLoadingIndicator(false)
                }

            }

            override fun onDataNotAvailable() {
                if (!mMedView.isActive) {
                    return
                }
                if (showLoadingUi) {
                    mMedView.showLoadingIndicator(false)
                }

                mMedView.showNoMedicine()
            }
        })
    }

    private fun processMedicineList(medicineAlarmList: List<MedicineAlarm>?) {
        if (medicineAlarmList!!.isEmpty()) {
            Log.d("med", "Med ALarm list empty")
            // Show a message indicating there are no tasks for that filter type.
            mMedView.showNoMedicine()
        } else {
            //Show the list of Medicines
            Collections.sort(medicineAlarmList)
            mMedView.showMedicineList(medicineAlarmList)
        }
    }

    init {
        mMedicineRepository = medicineRepository
        mMedView = medView
        medView.setPresenter(this)
    }
}