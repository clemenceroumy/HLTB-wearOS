package com.croumy.hltb_wearos.presentation.workers.interfaces

import android.content.Context
import androidx.work.Data
import androidx.work.ListenableWorker

interface IWorkerHelper {
     fun launchWorker(
        workerClass: Class<out ListenableWorker>,
        data: Data? = null,
        context: Context,
        name: String
    )
}