package com.croumy.hltb_wearos.presentation.helpers

import com.soywiz.klock.Time
import java.text.SimpleDateFormat
import java.util.Date

const val DATE_FORMAT = "dd/MM/yyyy"
const val DATETIME_FORMAT = "dd/MM/yyyy HH:mm"
const val ISO8601_DATE_FORMAT = "yyyy-MM-dd"

fun Time.asString(
    withSeconds: Boolean = false,
    withZeros: Boolean = true,
    withStringUnit: Boolean = false
): String {
    val secondsUnit = if(withStringUnit) "s" else ""
    val minutesUnit = if(withStringUnit) "m" else ":"
    val hoursUnit = if(withStringUnit) "h" else ":"

    return if(withZeros) {
        "${this.hour}$hoursUnit${this.minute}$minutesUnit${if (withSeconds) "${this.second}$secondsUnit" else ""}"
    } else {
        "${if(this.hour > 0) "${this.hour.toString().padStart(2, '0')}$hoursUnit" else ""}${this.minute.toString().padStart(2, '0')}$minutesUnit${this.second.toString().padStart(2, '0')}$secondsUnit"
    }
}

fun Date.asString(): String {
    return SimpleDateFormat(DATETIME_FORMAT).format(this)
}

fun String.asDate(format: String = DATETIME_FORMAT): Date {
    return SimpleDateFormat(format).parse(this)
}