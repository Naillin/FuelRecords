package com.example.fuelrecords

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

    public fun createSimpleDialog(thisActivity: AppCompatActivity, title: String = "default", msg: String = "default") {
        val builder = AlertDialog.Builder(thisActivity)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.show()
    }

    public fun createYesNoDialog(thisActivity: AppCompatActivity, title: String = "default", msg: String = "default", onYesClicked: () -> Unit, onNoClicked: () -> Unit) {
        val builder = AlertDialog.Builder(thisActivity)
        builder.setTitle(title)
        builder.setMessage(msg)

        builder.setPositiveButton("YES") { _, _ ->
            onYesClicked.invoke()
        }

        builder.setNegativeButton("NO") { _, _ ->
            onNoClicked.invoke()
        }

        builder.show()
    }
}