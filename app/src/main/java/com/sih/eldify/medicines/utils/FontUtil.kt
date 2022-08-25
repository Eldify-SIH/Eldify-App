package com.sih.eldify.medicines.utils

import android.graphics.Typeface
import com.sih.eldify.mediciness.MedicineApp
import java.util.*


object FontUtil {
    const val ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf"
    const val ROBOTO_LIGHT = "fonts/Roboto-Light.ttf"
    const val ROBOTO_BOLD = "fonts/Roboto-Bold.ttf"

    // Cache fonts in hash table
    private val fontCache = Hashtable<String, Typeface?>()
    fun getTypeface(name: String): Typeface? {
        var tf = fontCache[name]
        if (tf == null) {
            tf = try {
                Typeface.createFromAsset(MedicineApp.instance!!.assets, name)
            } catch (e: Exception) {
                return null
            }
            fontCache[name] = tf
        }
        return tf
    }
}