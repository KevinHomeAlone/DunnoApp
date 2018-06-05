package com.pieprzak.kevin.dunno.Utilities

import android.content.Context
import android.widget.EditText
import com.pieprzak.kevin.dunno.R

object Validators {

    /**
     * Validates any text field and checks if it's blank
     */
    fun validateAnyTextField(context: Context, textField: EditText): Boolean {
        val text = textField.text.toString()

        return when {
            text.isBlank() -> {
                textField.error = context.getString(R.string.error_field_cant_be_empty)
                false
            }
            else -> true
        }
    }
}