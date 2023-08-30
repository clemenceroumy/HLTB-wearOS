package com.croumy.hltb_wearos.presentation.data

import SubmitRequest
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.Room
import com.croumy.hltb_wearos.presentation.data.database.AppDatabase
import com.croumy.hltb_wearos.presentation.data.database.dao.LogDao
import com.croumy.hltb_wearos.presentation.data.database.entity.LogEntity
import com.croumy.hltb_wearos.presentation.models.Timer
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.soywiz.klock.DateTime
import com.soywiz.klock.milliseconds
import com.soywiz.klock.plus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.fixedRateTimer

@Singleton
class AppService @Inject constructor(
    private val logDao: LogDao,
    preferencesService: PreferencesService
) {
    val isLoggedIn = MutableStateFlow(preferencesService.token != null && preferencesService.token?.isNotEmpty() == true)
    val isLoggingIn = MutableStateFlow(false)

    val timer = mutableStateOf(Timer())
    private var stopwatch: java.util.Timer = java.util.Timer()
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

    fun saveLog(log: LogEntity, isRetrying: Boolean = false) {
        if(timer.value.gameId == null || submitRequest.value == null) return

        if(isRetrying) {
            logDao.update(log)
        } else {
            logDao.insert(log)
        }
    }
}