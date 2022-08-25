package com.sih.eldify.medicines.utils

import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**
 * Created by gautam on 13/07/17.
 */
object ActivityUtils {
    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.
     */
    fun addFragmentToActivity(
        fragmentManager: FragmentManager,
        fragment: Fragment, frameId: Int
    ) {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    fun setBackGround(context: Context?, view: View, @DrawableRes drawable: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = ContextCompat.getDrawable(context!!, drawable)
        } else {
            view.setBackgroundDrawable(ContextCompat.getDrawable(context!!, drawable))
        }
    }
}