package com.sih.eldify.medicines.data.source

import java.util.*

/**
 * Created by gautam on 13/07/17.
 */
class History {
    var hourTaken: Int = 0
    var minuteTaken: Int = 0
    var dateString: String? = null
    var pillName: String? = null
    var action: Int = 0
    var doseQuantity: String? = null
    var doseUnit: String? = null
    var alarmId: Int = 0

    constructor() {}
    constructor(
        hourTaken: Int,
        minuteTaken: Int,
        dateString: String?,
        pillName: String?,
        action: Int,
        doseQuantity: String?,
        doseUnit: String?,
        alarmId: Int
    ) {
        this.hourTaken = hourTaken
        this.minuteTaken = minuteTaken
        this.dateString = dateString
        this.pillName = pillName
        this.action = action
        this.doseQuantity = doseQuantity
        this.doseUnit = doseUnit
        this.alarmId = alarmId
    }

    val am_pmTaken: String
        get() = if ((hourTaken < 12)) "am" else "pm"

    /**
     * A helper method which returns the time of the alarm in string form
     * hour:minutes am/pm
     */
    val stringTime: String
        get() {
            var nonMilitaryHour: Int = hourTaken % 12
            if (nonMilitaryHour == 0) nonMilitaryHour = 12
            var min: String? = Integer.toString(minuteTaken)
            if (minuteTaken < 10) min = "0$minuteTaken"
            return String.format(
                Locale.getDefault(), "%d:%s %s", nonMilitaryHour, min,
                am_pmTaken
            )
        }
    val formattedDate: String
        get() = String.format(
            Locale.getDefault(), "%s %s", dateString,
            stringTime
        )

    /**
     * A helper method which returns the formatted medicine dose
     * doseQuantity doseUnit
     */
    val formattedDose: String
        get() {
            return String.format(Locale.getDefault(), "%s %s", doseQuantity, doseUnit)
        }
}