package com.croumy.hltb_wearos.presentation.ui.home.logs

import General
import Lists
import SubmitRequest
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.PreferencesService
import com.croumy.hltb_wearos.presentation.data.database.dao.LogDao
import com.croumy.hltb_wearos.presentation.data.database.entity.LogEntity
import com.croumy.hltb_wearos.presentation.helpers.extensions.asString
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.workers.SaveTimeWorker
import com.croumy.hltb_wearos.presentation.workers.WorkerHelper
import com.croumy.hltb_wearos.presentation.workers.interfaces.IWorkerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesService: PreferencesService,
    private val logDao: LogDao,
    val appService: AppService,
    private val workerHelper: IWorkerHelper
) : ViewModel() {
    val logs = mutableStateOf(emptyList<LogEntity>())
    val succeededLogs: List<LogEntity> get() = logs.value.filter { it.saved }
    val failedLogs: List<LogEntity> get() = logs.value.filter { !it.saved }
    val isLoading = mutableStateOf(false)

    suspend fun getLogs() {
        isLoading.value = true
        logs.value = logDao.getAll().sortedBy { it.date }.reversed()
        isLoading.value = false
    }

    fun deleteLog(log: LogEntity) {
        viewModelScope.launch {
            logDao.delete(log)
            getLogs()
        }
    }

    fun deleteAllLogs() {
        viewModelScope.launch {
            logDao.deleteAll()
            getLogs()
        }
    }

    fun resend(log: LogEntity) {
        appService.timer.value = appService.timer.value.copy(
            state = TimerState.SAVING,
            gameId = log.gameId,
            id = log.submissionId,
            time = log.timePlayedTime
        )
        appService.submitRequest.value = SubmitRequest(
            submissionId = log.submissionId,
            userId = preferencesService.userId!!,
            gameId = log.gameId,
            title = log.title,
            platform = log.platform,
            storefront = log.storefront,
            lists = Lists(),
            general = General(
                progress = Progress.fromLong(log.progress),
                progressBefore = Progress.fromLong(log.progressBefore)
            )
        )

        val data = Data.Builder()
        data.putBoolean("IS_RETRYING", true)
        data.putInt("LOG_ID", log.id)
        data.putString("DATE", log.date.asString())

        workerHelper.launchWorker(SaveTimeWorker::class.java, data = data.build(), context = context, name = "saveTime")
    }
}