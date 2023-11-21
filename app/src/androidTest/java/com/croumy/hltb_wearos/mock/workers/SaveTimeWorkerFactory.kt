package com.croumy.hltb_wearos.mock.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.croumy.hltb_wearos.presentation.data.interfaces.IAppService
import com.croumy.hltb_wearos.presentation.data.interfaces.IHLTBService
import com.croumy.hltb_wearos.presentation.workers.SaveTimeWorker
import javax.inject.Inject

class SaveTimeWorkerFactory @Inject constructor(
    private val hltbService: IHLTBService,
    private val appService: IAppService
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return SaveTimeWorker(
            appContext,
            workerParameters,
            hltbService,
            appService
        )
    }
}