package com.sih.eldify.medicines.medicine

import android.content.Context


/**
 * Created by gautam on 13/07/17.
 */
interface MedicineContract {
    interface View : BaseView<com.sih.eldify.medicines.medicine.MedicineContract.Presenter?> {
        fun showLoadingIndicator(active: Boolean)
        fun showMedicineList(medicineAlarmList: List<MedicineAlarm?>?)
        fun showAddMedicine()
        fun showMedicineDetails(medId: Long, medName: String?)
        fun showLoadingMedicineError()
        fun showNoMedicine()
        fun showSuccessfullySavedMessage()
        fun showMedicineDeletedSuccessfully()
        val isActive: Boolean
    }

    interface Presenter : BasePresenter {
        fun onStart(day: Int)
        fun reload(day: Int)
        fun result(requestCode: Int, resultCode: Int)
        fun loadMedicinesByDay(day: Int, showIndicator: Boolean)
        fun deleteMedicineAlarm(medicineAlarm: MedicineAlarm?, context: Context?)
        fun addNewMedicine()
    }
}