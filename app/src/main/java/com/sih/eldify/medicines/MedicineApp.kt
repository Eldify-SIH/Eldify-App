package com.sih.eldify.medicines

import android.app.Application
import android.content.Context


/**
 * Created by gautam on 12/07/17.
 */
class MedicineApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (instance == null) {
            instance = applicationContext
        }
    }

    companion object {
        var instance: Context? = null
            private set
    }
}
