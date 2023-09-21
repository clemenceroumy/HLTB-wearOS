package com.croumy.hltb_wearos.presentation.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.data.database.entity.LogEntity
import com.croumy.hltb_wearos.presentation.helpers.asDate
import com.croumy.hltb_wearos.presentation.models.TimerState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar

@HiltWorker
class SaveTimeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val hltbService: HLTBService,
    private val appService: AppService
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val isRetrying = inputData.getBoolean("IS_RETRYING", false)
        val logId = inputData.getInt("LOG_ID", 0)
        val date = inputData.getString("DATE")

        val log = LogEntity(
            id = logId,
            gameId = appService.timer.value.gameId!!,
            submissionId = appService.submitRequest.value!!.submissionId,
            timePlayed = appService.timer.value.time.encoded.millisecondsLong,
            date = date?.asDate() ?: Calendar.getInstance().time,
            saved = false,
            title = appService.submitRequest.value!!.title,
            platform = appService.submitRequest.value!!.platform,
            storefront = appService.submitRequest.value!!.storefront,
            progress = appService.submitRequest.value!!.general.progress.toTime().encoded.millisecondsLong,
            progressBefore = appService.submitRequest.value!!.general.progressBefore.toTime().encoded.millisecondsLong
        )

        return try {
            hltbService.submitTime(appService.submitRequest.value!!)

            appService.timer.value = appService.timer.value.copy(state = TimerState.SAVED)
            // RESET OF TIMER IN LAUNCH EFFECT IN GAMEDETAILS.kt

            // SAVE AS LOG IN LOCAL DB
            log.saved = true
            appService.saveLog(log, isRetrying = isRetrying)

            Result.success()
        } catch (e: Exception) {
            Log.e("SaveTimeWorker", e.message.toString())
            appService.timer.value = appService.timer.value.copy(state = TimerState.ERROR)

            // SAVE AS LOG IN LOCAL DB (WITH ERROR)
            appService.saveLog(log, isRetrying = isRetrying)
            Result.failure()
        }
    }
}