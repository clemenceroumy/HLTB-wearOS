package com.croumy.hltb_wearos.presentation.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.croumy.hltb_wearos.presentation.models.Timer
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.soywiz.klock.milliseconds
import com.soywiz.klock.plus
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppService @Inject constructor() {
    val timer = mutableStateOf(Timer())

    suspend fun startTimer() {
        timer.value = timer.value.copy(state = TimerState.STARTED)
        while (timer.value.state == TimerState.STARTED) {
            delay(10)
            timer.value = timer.value.copy(time = timer.value.time.plus(10.milliseconds))
            Log.i("Tick", "Timer: ${timer.value.time}")
        }
    }
}