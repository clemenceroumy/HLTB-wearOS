package com.croumy.hltb_wearos.presentation.helpers

import com.soywiz.klock.Time

fun Time.asString(): String {
    return "${this.hour}h${this.minute}m"
}