package com.sih.eldify.medicines.data.source

import java.util.*

class MedicineAlarm : Comparable<MedicineAlarm?> {
    var id // DB id number
            : Long = 0
    var hour //
            = 0
    var minute = 0
    var pillName: String? = null
    var doseQuantity: String? = null
    var doseUnit: String? = null
    var dateString: String? = null
    var alarmId = 0

    constructor() {}
    constructor(
        id: Long,
        hour: Int,
        minute: Int,
        pillName: String?,
        doseQuantity: String?,
        doseUnit: String?,
        alarmId: Int
    ) {
        this.id = id
        this.hour = hour
        this.minute = minute
        this.pillName = pillName
        this.doseQuantity = doseQuantity
        this.doseUnit = doseUnit
        this.alarmId = alarmId
    }

    private val ids: MutableList<Long> = LinkedList()
    var dayOfWeek = BooleanArray(7)

    fun getIds(): List<Long> {
        return Collections.unmodifiableList(ids)
    }

    fun addId(id: Long) {
        ids.add(id)
    }

    private val am_pm: String
        private get() = if (hour < 12) "am" else "pm"

    /**
     * Overrides the compareTo() method so that alarms can be sorted by time of day from earliest to
     * latest.
     */
    override operator fun compareTo(medicineAlarm: MedicineAlarm): Int {
        return if (hour < medicineAlarm.hour) -1 else if (hour > medicineAlarm.hour) 1 else {
            if (minute < medicineAlarm.minute) -1 else if (minute > medicineAlarm.minute) 1 else 0
        }
    }


    /**
     * A helper method which returns the time of the alarm in string form
     * hour:minutes am/pm
     */
    val stringTime: String
        get() {
            var nonMilitaryHour = hour % 12
            if (nonMilitaryHour == 0) nonMilitaryHour = 12
            var min = Integer.toString(minute)
            if (minute < 10) min = "0$minute"
            return String.format(
                Locale.getDefault(), "%d:%s %s", nonMilitaryHour, min,
                am_pm
            )
        }

    val formattedDose: String
        get() = String.format(Locale.getDefault(), "%s %s", doseQuantity, doseUnit)



}