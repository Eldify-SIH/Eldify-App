package com.sih.eldify.medicines.data.source

import java.util.*

class Pills {
    var pillName: String? = null
    var pillId: Long = 0
    private val medicineAlarms: MutableList<MedicineAlarm> = LinkedList()

    constructor() {}
    constructor(pillName: String?, pillId: Long) {
        this.pillName = pillName
        this.pillId = pillId
    }

    /**
     *
     * @param medicineAlarm
     * allows a new medicineAlarm sto be added to a preexisting medicineAlarm
     */
    fun addAlarm(medicineAlarm: MedicineAlarm) {
        medicineAlarms.add(medicineAlarm)
        Collections.sort(medicineAlarms)
    }
}