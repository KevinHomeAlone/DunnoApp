package com.pieprzak.kevin.dunno.Utilities

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Tools {

    fun parseDate(string: String) : Date {
        val year = string.substring(0,4).toInt()
        val month = string.substring(5,7).toInt() - 1
        val day = string.substring(8,10).toInt()
        val hour = string.substring(11,13).toInt()
        val minutes = string.substring(14,16).toInt()
        val seconds = string.substring(17,19).toInt()

        val date = Calendar.getInstance()
        date.set(year, month, day,hour,minutes, seconds)
        //Log.d("Month", "$month, ${date.time.month}")

        return date.time
    }

    fun dateToString(date : Date): String{
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US)

        return format.format(date)
    }
}