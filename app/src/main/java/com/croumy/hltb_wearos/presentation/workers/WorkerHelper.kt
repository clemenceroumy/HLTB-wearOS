package com.croumy.hltb_wearos.presentation.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.croumy.hltb_wearos.presentation.workers.interfaces.IWorkerHelper
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
class WorkerHelper: IWorkerHelper {
     override fun launchWorker(
         workerClass: Class<out ListenableWorker>,
         data: Data?,
         context: Context,
         name: String
    ) {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadWorkRequest: OneTimeWorkRequest = OneTimeWorkRequest.Builder(workerClass)
            .addTag(name)
            .setInputData(data ?: Data.EMPTY)
            .setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(context)
            .enqueue(uploadWorkRequest)
    }
}
