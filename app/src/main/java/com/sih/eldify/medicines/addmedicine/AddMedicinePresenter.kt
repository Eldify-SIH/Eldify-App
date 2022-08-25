package com.sih.eldify.mediciness.addmedicine

import com.sih.eldify.medicines.data.source.MedicineAlarm
import com.sih.eldify.medicines.data.source.MedicineDataSource
import com.sih.eldify.medicines.data.source.Pills


/**
 * Created by gautam on 12/07/17.
 */
class AddMedicinePresenter(
    private val mMedicineId: Int,
    mMedicineRepository: MedicineDataSource,
    mAddMedicineView: AddMedicineContract.View,
    mIsDataMissing: Boolean
) :
    AddMedicineContract.Presenter, MedicineDataSource.GetTaskCallback {
    private val mMedicineRepository: MedicineDataSource
    private val mAddMedicineView: AddMedicineContract.View
    override val isDataMissing: Boolean
    private val isNewTask: Boolean
        private get() = mMedicineId <= 0

    override fun start() {}
    override fun saveMedicine(alarm: MedicineAlarm?, pills: Pills?) {
        mMedicineRepository.saveMedicine(alarm, pills)
    }

    override fun isMedicineExits(pillName: String?): Boolean {
        return mMedicineRepository.medicineExits(pillName)
    }

    override fun addPills(pills: Pills?): Long {
        return mMedicineRepository.savePills(pills)
    }

    override fun getPillsByName(pillName: String?): Pills {
        return mMedicineRepository.getPillsByName(pillName)!!
    }

    override fun getMedicineByPillName(pillName: String?): List<MedicineAlarm> {
        return mMedicineRepository.getMedicineByPillName(pillName) as List<MedicineAlarm>
    }

    override fun tempIds(): List<Long> {
        return mMedicineRepository.tempIds() as List<Long>
    }

    override fun deleteMedicineAlarm(alarmId: Long) {
        mMedicineRepository.deleteAlarm(alarmId)
    }

    override fun onTaskLoaded(medicineAlarm: MedicineAlarm?) {
        // The view may not be able to handle UI updates anymore
        /*if (mAddMedicineView.isActive()){
            mAddMedicineView.setDose(medicineAlarm.getDose());
            mAddMedicineView.setMedName(medicineAlarm.getMedicineName());
            mAddMedicineView.setDays(medicineAlarm.getDays());
            mAddMedicineView.setTime(medicineAlarm.getTime());
        }
        mIsDataMissing = false;*/
    }

    override fun onDataNotAvailable() {
        if (mAddMedicineView.isActive) {
            mAddMedicineView.showEmptyMedicineError()
        }
    }

    init {
        this.mMedicineRepository = mMedicineRepository
        this.mAddMedicineView = mAddMedicineView
        isDataMissing = mIsDataMissing
        mAddMedicineView.setPresenter(this)
    }
}
