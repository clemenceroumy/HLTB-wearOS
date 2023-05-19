package com.croumy.hltb_wearos.presentation.models

import com.soywiz.klock.Time
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.seconds

data class Timer(
    var state: TimerState = TimerState.IDLE,
    var time: Time = Time(0, 0, 0)
) {
    val progress: Float get() = (time.second / 60f)
}
