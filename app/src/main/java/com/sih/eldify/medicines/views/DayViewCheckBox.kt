package com.sih.eldify.medicines.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import com.sih.eldify.R

class DayViewCheckBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatCheckBox(context, attrs) {



    override fun setChecked(t: Boolean) {
        if (t) {
            this.setTextColor(Color.WHITE)
        } else {
            this.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
        }
        super.setChecked(t)
    }
}
