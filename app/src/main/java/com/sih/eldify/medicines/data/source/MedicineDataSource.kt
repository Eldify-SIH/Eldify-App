package com.sih.eldify.medicines.data.source

/**
 * Created by gautam on 13/07/17.
 */
interface MedicineDataSource {
    interface LoadMedicineCallbacks {
        fun onMedicineLoaded(medicineAlarmList: List<MedicineAlarm?>?)
        fun onDataNotAvailable()
    }

    interface GetTaskCallback {
        fun onTaskLoaded(medicineAlarm: MedicineAlarm?)
        fun onDataNotAvailable()
    }

    interface LoadHistoryCallbacks {
        fun onHistoryLoaded(historyList: List<History?>?)
        fun onDataNotAvailable()
    }

    fun getMedicineHistory(loadHistoryCallbacks: LoadHistoryCallbacks?)
    fun getMedicineAlarmById(id: Long, callback: GetTaskCallback?)
    fun saveMedicine(medicineAlarm: MedicineAlarm?, pills: Pills?)
    fun getMedicineListByDay(day: Int, callbacks: LoadMedicineCallbacks?)
    fun medicineExits(pillName: String?): Boolean
    fun tempIds(): List<Long?>?
    fun deleteAlarm(alarmId: Long)
    fun getMedicineByPillName(pillName: String?): List<MedicineAlarm?>?
    fun getAllAlarms(pillName: String?): List<MedicineAlarm?>?
    fun getPillsByName(pillName: String?): Pills?
    fun savePills(pills: Pills?): Long
    fun saveToHistory(history: History?)
}