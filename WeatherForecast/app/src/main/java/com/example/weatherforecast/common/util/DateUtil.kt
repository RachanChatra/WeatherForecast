package com.example.weatherforecast.common.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    val DATE_TIME_FORMAT_12_HOURS = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US)
    val TIME_FORMAT_12_HOURS = SimpleDateFormat("hh:mm a", Locale.US)

    fun getDate(time: Long?): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = (time?:0 * 1000) ?:calendar.timeInMillis
        return TIME_FORMAT_12_HOURS.format(calendar)
    }
}