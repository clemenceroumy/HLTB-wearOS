package com.croumy.hltb_wearos.presentation.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class WorkerHelper {
    companion object {
        inline fun <reified W : ListenableWorker> launchWorker(
            data: Data? = null,
            context: Context,
            name: String
        ) {
            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val uploadWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<W>()
                .addTag(name)
                .setInputData(data ?: Data.EMPTY)
                .setConstraints(constraints)
                .build()

            WorkManager
                .getInstance(context)
                .enqueue(uploadWorkRequest)
        }
    }
}
