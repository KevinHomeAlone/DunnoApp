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

    fun validateTextFieldLength(context: Context, textField: EditText, length: Int): Boolean{
        val text = textField.text.toString()

        return when{
            text.length > length -> {
                textField.error = (context.getString(R.string.over_the_max_chars_count) + " " + length.toString())
                false
            }
            else -> true
        }
    }

    fun validatePasswordTextField(context: Context, textField: EditText): Boolean{
        val text = textField.text.toString()

        return when {
            text.isBlank() || text.length < 8 -> {
                textField.error = context.getString(R.string.password_have_to_be_longer)
                false
            }
            else -> true
        }
    }
}