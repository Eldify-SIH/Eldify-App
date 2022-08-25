package com.sih.eldify.medicines.alarm

import com.sih.eldify.medicines.BasePresenter
import com.sih.eldify.medicines.BaseView
import com.sih.eldify.medicines.data.source.History
import com.sih.eldify.medicines.data.source.MedicineAlarm


/**
 * Created by gautam on 13/07/17.
 */
interface ReminderContract {
    interface View : BaseView<Presenter?> {
        fun showMedicine(medicineAlarm: MedicineAlarm?)
        fun showNoData()
        val isActive: Boolean

        fun onFinish()
    }

    interface Presenter : BasePresenter {
        fun finishActivity()
        fun onStart(id: Long)
        fun loadMedicineById(id: Long)
        fun addPillsToHistory(history: History?)
    }
}
