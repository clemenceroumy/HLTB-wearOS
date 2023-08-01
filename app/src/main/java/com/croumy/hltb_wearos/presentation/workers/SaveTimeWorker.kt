package com.croumy.hltb_wearos.presentation.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.Timer
import com.croumy.hltb_wearos.presentation.models.TimerState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SaveTimeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val hltbService: HLTBService,
    private val appService: AppService
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            Log.i("SaveTimeWorker", "Saving time: ${appService.submitRequest.value!!}")
            hltbService.submitTime(appService.submitRequest.value!!)

            appService.timer.value = appService.timer.value.copy(state = TimerState.SAVED)
            // RESET OF TIMER IN LAUNCH EFFECT IN GAMEDETAILS.kt

            // SAVE AS LOG IN LOCAL DB
            appService.saveLog(true)

            Result.success()
        } catch (e: Exception) {
            Log.e("SaveTimeWorker", e.message.toString())
            // SAVE AS LOG IN LOCAL DB (WITH ERROR)
            appService.saveLog(false)
            Result.failure()
        }
    }
}