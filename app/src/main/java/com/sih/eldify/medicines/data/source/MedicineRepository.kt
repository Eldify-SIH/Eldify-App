package com.sih.eldify.medicines.data.source

class MedicineRepository private constructor(private val localDataSource: MedicineDataSource) :
    MedicineDataSource {

    override fun getMedicineHistory(loadHistoryCallbacks: LoadHistoryCallbacks) {
        localDataSource.getMedicineHistory(object : LoadHistoryCallbacks() {
            fun onHistoryLoaded(historyList: List<History?>?) {
                loadHistoryCallbacks.onHistoryLoaded(historyList)
            }

            fun onDataNotAvailable() {
                loadHistoryCallbacks.onDataNotAvailable()
            }
        })
    }

    override fun getMedicineAlarmById(id: Long, callback: GetTaskCallback) {
        localDataSource.getMedicineAlarmById(id, object : GetTaskCallback() {
            fun onTaskLoaded(medicineAlarm: MedicineAlarm?) {
                if (medicineAlarm == null) {
                    return
                }
                callback.onTaskLoaded(medicineAlarm)
            }

            fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun saveMedicine(medicineAlarm: MedicineAlarm?, pills: Pills?) {
        localDataSource.saveMedicine(medicineAlarm, pills)
    }

    override fun getMedicineListByDay(day: Int, callbacks: LoadMedicineCallbacks) {
        localDataSource.getMedicineListByDay(day, object : LoadMedicineCallbacks() {
            fun onMedicineLoaded(medicineAlarmList: List<MedicineAlarm?>?) {
                callbacks.onMedicineLoaded(medicineAlarmList)
            }

            fun onDataNotAvailable() {
                callbacks.onDataNotAvailable()
            }
        })
    }

    override fun medicineExits(pillName: String?): Boolean {
        return false
    }

    override fun tempIds(): List<Long?>? {
        return localDataSource.tempIds()
    }

    override fun deleteAlarm(alarmId: Long) {
        localDataSource.deleteAlarm(alarmId)
    }

    override fun getMedicineByPillName(pillName: String?): List<MedicineAlarm?>? {
        return localDataSource.getMedicineByPillName(pillName)
    }

    override fun getAllAlarms(pillName: String?): List<MedicineAlarm?>? {
        return localDataSource.getAllAlarms(pillName)
    }

    override fun getPillsByName(pillName: String?): Pills? {
        return localDataSource.getPillsByName(pillName)
    }

    override fun savePills(pills: Pills?): Long {
        return localDataSource.savePills(pills)
    }

    override fun saveToHistory(history: History?) {
        localDataSource.saveToHistory(history)
    }

    companion object {
        private var mInstance: MedicineRepository? = null
        fun getInstance(localDataSource: MedicineDataSource): MedicineRepository? {
            if (mInstance == null) {
                mInstance = MedicineRepository(localDataSource)
            }
            return mInstance
        }
    }
}
