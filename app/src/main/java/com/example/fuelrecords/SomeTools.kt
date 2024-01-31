package com.example.fuelrecords

import android.content.Context
import com.google.android.material.textfield.TextInputEditText

class SomeTools(private val context: Context) {
    fun isFieldsEmpty(textInputsList: ArrayList<TextInputEditText>): Boolean {
        var result: Boolean = false

        for(item in textInputsList) {
            if(item.text.isNullOrEmpty()) {
                item.error = context.getString(R.string.textview_input_error)
                result = true
            }
        }

        return result
    }
}