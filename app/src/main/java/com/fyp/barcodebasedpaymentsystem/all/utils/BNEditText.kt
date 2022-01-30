package com.fyp.barcodebasedpaymentsystem.all.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class BNEditText(context: Context, attrs: AttributeSet):AppCompatEditText(context, attrs){

    init {
        applyFont()
    }

    private fun applyFont() {
        val typeface: Typeface =
                Typeface.createFromAsset(context.assets, "BebasNeue-Regular.ttf")
        setTypeface(typeface)
    }



}