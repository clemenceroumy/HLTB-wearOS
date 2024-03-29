package com.croumy.hltb_wearos.mock.services

import SubmitRequest
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.interfaces.IAppService
import com.croumy.hltb_wearos.presentation.data.database.entity.LogEntity
import com.croumy.hltb_wearos.presentation.models.Timer
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.soywiz.klock.milliseconds
import com.soywiz.klock.plus
import com.soywiz.klock.seconds
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

class MockAppService : IAppService {
    override val isLoggedIn = MutableStateFlow(true)
    override val isLoggingIn = MutableStateFlow(false)

    override val timer = mutableStateOf(Timer())
    override var stopwatch: java.util.Timer = java.util.Timer()
    override val submitRequest = mutableStateOf<SubmitRequest?>(null)

    override fun clearTimer() {
        Log.i("MockAppService", "clearTimer()")
        timer.value = Timer()
        submitRequest.value = null
    }

    override fun startTimer() {
        Log.i("MockAppService", "startTimer()")
        timer.value = timer.value.copy(state = TimerState.STARTED)
        /* TODO: Using `fixedRateTimer(initialDelay = 10L, period = 10L)` (as in AppService) causes test to loop
         temp while checking how to use it (if possible) */
        timer.value = timer.value.copy(time = timer.value.time.plus(2.seconds))
    }

    override fun pauseTimer() {
        Log.i("MockAppService", "pauseTimer()")
        stopwatch.cancel()
        timer.value = timer.value.copy(state = TimerState.PAUSED)
    }

    override fun stopTimer() {
        Log.i("MockAppService", "stopTimer()")
        stopwatch.cancel()
        timer.value = timer.value.copy(state = TimerState.STOPPED)
    }

    override fun saveLog(log: LogEntity, isRetrying: Boolean) {
        Log.i("MockAppService", "saveLog()")
    }
}