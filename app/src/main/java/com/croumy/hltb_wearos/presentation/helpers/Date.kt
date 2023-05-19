package com.croumy.hltb_wearos.presentation.helpers

import com.soywiz.klock.Time

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