package com.croumy.hltb_wearos.presentation.data.interfaces

import SubmitRequest
import androidx.compose.runtime.MutableState
import com.croumy.hltb_wearos.presentation.data.database.entity.LogEntity
import com.croumy.hltb_wearos.presentation.models.Timer
import kotlinx.coroutines.flow.MutableStateFlow

interface IAppService {
    val isLoggedIn: MutableStateFlow<Boolean>
    val isLoggingIn: MutableStateFlow<Boolean>

    val timer: MutableState<Timer>
    var stopwatch: java.util.Timer
    val submitRequest: MutableState<SubmitRequest?>

    fun clearTimer()
    fun startTimer()
    fun pauseTimer()
    fun stopTimer()
    fun saveLog(log: LogEntity, isRetrying: Boolean = false)
}