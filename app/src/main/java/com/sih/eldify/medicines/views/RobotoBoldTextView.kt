package com.sih.eldify.medicines.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.sih.eldify.medicines.utils.FontUtil


class RobotoBoldTextView : AppCompatTextView {
    constructor(context: Context?) : super(context!!) {
        applyCustomFont()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        applyCustomFont()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
        applyCustomFont()
    }

    private fun applyCustomFont() {
        val customFont: Typeface? = FontUtil.getTypeface(FontUtil.ROBOTO_BOLD)
        setTypeface(customFont)
    }
}