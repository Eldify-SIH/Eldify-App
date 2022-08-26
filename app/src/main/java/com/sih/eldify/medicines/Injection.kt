package com.sih.eldify.medicines

import android.content.Context

import com.sih.eldify.medicines.data.source.MedicineRepository
import com.sih.eldify.medicines.data.source.local.MedicinesLocalDataSource

/**
 * Created by gautam on 13/07/17.
 */
object Injection {
    fun provideMedicineRepository(context: Context): MedicineRepository {
        return MedicineRepository.getInstance(MedicinesLocalDataSource.getInstance(context)!!)!!
    }
}