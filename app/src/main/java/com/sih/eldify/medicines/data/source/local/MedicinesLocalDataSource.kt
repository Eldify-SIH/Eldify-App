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
    override fun getMedicineHistory(loadHistoryCallbacks: MedicineDataSource.LoadHistoryCallbacks?) {
        val historyList: List<History> = mDbHelper.history as List<History>
        loadHistoryCallbacks!!.onHistoryLoaded(historyList)
    }

    override fun getMedicineAlarmById(id: Long, callback: MedicineDataSource.GetTaskCallback?) {
        try {
            val medicineAlarm: MedicineAlarm = getAlarmById(id)
            if (medicineAlarm != null) {
                callback!!.onTaskLoaded(medicineAlarm)
            } else {
                callback!!.onDataNotAvailable()
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            callback!!.onDataNotAvailable()
        }
    }

    override fun saveMedicine(medicineAlarm: MedicineAlarm?, pill: Pills?) {
        mDbHelper.createAlarm(medicineAlarm!!, pill!!.pillId)
    }

    override fun getMedicineListByDay(day: Int, callbacks: MedicineDataSource.LoadMedicineCallbacks?) {
        val medicineAlarmList: List<MedicineAlarm> = mDbHelper.getAlarmsByDay(day)
        callbacks!!.onMedicineLoaded(medicineAlarmList)
    }

    override fun medicineExits(pillName: String?): Boolean {
        for (pill in Pills.getPillName()) {
            if (pill.getPillName().equals(pillName)) return true
        }
        return false
    }

    override fun tempIds(): List<Long>? {
        return null
    }

    override fun deleteAlarm(alarmId: Long) {
        deleteAlarmById(alarmId)
    }

    override fun getMedicineByPillName(pillName: String?): List<MedicineAlarm>? {
        return try {
            getMedicineByPill(pillName!!)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            null
        }
    }

    override fun getAllAlarms(pillName: String?): List<MedicineAlarm>? {
        return try {
            getAllAlarmsByName(pillName!!)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            null
        }
    }

    override fun getPillsByName(pillName: String?): Pills {
        return getPillByName(pillName!!)
    }

    override fun savePills(pills: Pills?): Long {
        return savePill(pills!!)
    }

    override fun saveToHistory(history: History?) {
        mDbHelper.createHistory(history!!)
    }

    private val pills: List<Any>
        private get() = mDbHelper.allPills

    private fun savePill(pill: Pills): Long {
        val pillId = mDbHelper.createPill(pill)
        pill.pillId = pillId
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
        mDbHelper.createHistory(h!!)
    }

    val history: List<Any>
        get() = mDbHelper.history

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