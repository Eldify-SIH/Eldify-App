package com.sih.eldify.medicines.data.source.local

import android.content.Context
import com.sih.eldify.medicines.data.source.History
import com.sih.eldify.medicines.data.source.MedicineAlarm
import com.sih.eldify.medicines.data.source.MedicineDataSource
import com.sih.eldify.medicines.data.source.Pills
import java.net.URISyntaxException

/**
 * Created by gautam on 13/07/17.
 */
class MedicinesLocalDataSource private constructor(context: Context) :
    MedicineDataSource {
    private val mDbHelper: MedicineDBHelper
    fun getMedicineHistory(loadHistoryCallbacks: LoadHistoryCallbacks) {
        val historyList: List<History> = mDbHelper.getHistory()
        loadHistoryCallbacks.onHistoryLoaded(historyList)
    }

    fun getMedicineAlarmById(id: Long, callback: GetTaskCallback) {
        try {
            val medicineAlarm: MedicineAlarm = getAlarmById(id)
            if (medicineAlarm != null) {
                callback.onTaskLoaded(medicineAlarm)
            } else {
                callback.onDataNotAvailable()
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            callback.onDataNotAvailable()
        }
    }

    fun saveMedicine(medicineAlarm: MedicineAlarm?, pill: Pills) {
        mDbHelper.createAlarm(medicineAlarm, pill.getPillId())
    }

    fun getMedicineListByDay(day: Int, callbacks: LoadMedicineCallbacks) {
        val medicineAlarmList: List<MedicineAlarm> = mDbHelper.getAlarmsByDay(day)
        callbacks.onMedicineLoaded(medicineAlarmList)
    }

    fun medicineExits(pillName: String?): Boolean {
        for (pill in pills) {
            if (pill.getPillName().equals(pillName)) return true
        }
        return false
    }

    fun tempIds(): List<Long>? {
        return null
    }

    fun deleteAlarm(alarmId: Long) {
        deleteAlarmById(alarmId)
    }

    fun getMedicineByPillName(pillName: String): List<MedicineAlarm>? {
        return try {
            getMedicineByPill(pillName)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            null
        }
    }

    fun getAllAlarms(pillName: String): List<MedicineAlarm>? {
        return try {
            getAllAlarmsByName(pillName)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            null
        }
    }

    fun getPillsByName(pillName: String): Pills {
        return getPillByName(pillName)
    }

    fun savePills(pills: Pills): Long {
        return savePill(pills)
    }

    fun saveToHistory(history: History?) {
        mDbHelper.createHistory(history)
    }

    private val pills: List<Any>
        private get() = mDbHelper.getAllPills()

    private fun savePill(pill: Pills): Long {
        val pillId = mDbHelper.createPill(pill)
        pill.setPillId(pillId)
        return pillId
    }

    private fun getPillByName(pillName: String): Pills {
        return mDbHelper.getPillByName(pillName)
    }

    @Throws(URISyntaxException::class)
    private fun getMedicineByPill(pillName: String): List<MedicineAlarm> {
        return mDbHelper.getAllAlarmsByPill(pillName)
    }

    @Throws(URISyntaxException::class)
    private fun getAllAlarmsByName(pillName: String): List<MedicineAlarm> {
        return mDbHelper.getAllAlarms(pillName)
    }

    @Throws(URISyntaxException::class)
    fun deletePill(pillName: String?) {
        mDbHelper.deletePill(pillName!!)
    }

    private fun deleteAlarmById(alarmId: Long) {
        mDbHelper.deleteAlarm(alarmId)
    }

    fun addToHistory(h: History?) {
        mDbHelper.createHistory(h)
    }

    val history: List<Any>
        get() = mDbHelper.getHistory()

    @Throws(URISyntaxException::class)
    private fun getAlarmById(alarm_id: Long): MedicineAlarm {
        return mDbHelper.getAlarmById(alarm_id)
    }

    @Throws(URISyntaxException::class)
    fun getDayOfWeek(alarm_id: Long): Int {
        return mDbHelper.getDayOfWeek(alarm_id)
    }

    companion object {
        private var mInstance: MedicinesLocalDataSource? = null
        fun getInstance(context: Context): MedicinesLocalDataSource? {
            if (mInstance == null) {
                mInstance = MedicinesLocalDataSource(context)
            }
            return mInstance
        }
    }

    init {
        mDbHelper = MedicineDBHelper(context)
    }
}