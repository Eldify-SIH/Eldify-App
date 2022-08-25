package com.sih.eldify.mediciness.addmedicine


import com.sih.eldify.medicines.BasePresenter
import com.sih.eldify.medicines.BaseView
import com.sih.eldify.medicines.data.source.MedicineAlarm
import com.sih.eldify.medicines.data.source.Pills


/**
 * Created by gautam on 12/07/17.
 */
interface AddMedicineContract {
    interface View : BaseView<Presenter?> {
        fun showEmptyMedicineError()
        fun showMedicineList()
        val isActive: Boolean
    }

    interface Presenter : BasePresenter {
        fun saveMedicine(alarm: MedicineAlarm?, pills: Pills?)
        val isDataMissing: Boolean

        fun isMedicineExits(pillName: String?): Boolean
        fun addPills(pills: Pills?): Long
        fun getPillsByName(pillName: String?): Pills?
        fun getMedicineByPillName(pillName: String?): List<MedicineAlarm?>?
        fun tempIds(): List<Long?>?
        fun deleteMedicineAlarm(alarmId: Long)
    }
}
