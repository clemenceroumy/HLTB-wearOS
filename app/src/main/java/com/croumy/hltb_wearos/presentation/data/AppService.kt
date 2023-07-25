package com.croumy.hltb_wearos.presentation.data

import SubmitRequest
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.croumy.hltb_wearos.presentation.models.Timer
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.soywiz.klock.Stopwatch
import com.soywiz.klock.milliseconds
import com.soywiz.klock.plus
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.fixedRateTimer

@Singleton
class AppService @Inject constructor() {
    val timer = mutableStateOf(Timer())
    var stopwatch: java.util.Timer = java.util.Timer()
    val submitRequest = mutableStateOf<SubmitRequest?>(null)

    fun clearTimer() {
        timer.value = Timer()
        submitRequest.value = null
    }


     fun startTimer() {
        timer.value = timer.value.copy(state = TimerState.STARTED)
        timer.value = timer.value.copy(time = timer.value.time.plus(10.milliseconds))

        stopwatch = fixedRateTimer(initialDelay = 10L, period = 10L) {
            timer.value = timer.value.copy(time = timer.value.time.plus(10.milliseconds))
            Log.i("Tick", "Timer: ${timer.value.time}")
        }
    }

    fun pauseTimer() {
        stopwatch.cancel()
        timer.value = timer.value.copy(state = TimerState.PAUSED)
    }

     fun stopTimer() {
        stopwatch.cancel()
        timer.value = timer.value.copy(state = TimerState.STOPPED)
    }
}