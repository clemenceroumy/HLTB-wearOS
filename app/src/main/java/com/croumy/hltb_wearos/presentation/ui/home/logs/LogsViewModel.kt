package com.croumy.hltb_wearos.presentation.ui.home.logs

import General
import Lists
import SubmitRequest
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.work.Data
import com.croumy.hltb_wearos.BuildConfig
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.database.dao.LogDao
import com.croumy.hltb_wearos.presentation.data.database.entity.LogEntity
import com.croumy.hltb_wearos.presentation.helpers.asString
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.workers.SaveTimeWorker
import com.croumy.hltb_wearos.presentation.workers.WorkerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logDao: LogDao,
    val appService: AppService
): ViewModel() {
    val logs = mutableStateOf(emptyList<LogEntity>())
    val succeededLogs: List<LogEntity> get() = logs.value.filter { it.saved }
    val failedLogs: List<LogEntity> get() = logs.value.filter { !it.saved }

    fun getLogs() {
        logs.value = logDao.getAll().sortedBy { it.date }.reversed()
    }

    fun deleteLog(log: LogEntity) {
        logDao.delete(log)
        getLogs()
    }

    fun deleteAllLogs() {
        logDao.deleteAll()
        getLogs()
    }

    fun resend(log: LogEntity) {
        appService.timer.value = appService.timer.value.copy(
            state = TimerState.SAVING,
            gameId = log.gameId,
            time = log.timePlayedTime
        )
        appService.submitRequest.value = SubmitRequest(
            submissionId = log.submissionId,
            userId = BuildConfig.USER_ID.toInt(), // TODO: Get from HLTB
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

        WorkerHelper.launchWorker<SaveTimeWorker>(data = data.build(), context = context, name = "saveTime")
    }
}