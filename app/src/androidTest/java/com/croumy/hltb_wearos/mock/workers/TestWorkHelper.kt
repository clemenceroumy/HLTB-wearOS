package com.croumy.hltb_wearos.mock.workers

import android.content.Context
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.croumy.hltb_wearos.presentation.workers.SaveTimeWorker
import com.croumy.hltb_wearos.presentation.workers.interfaces.IWorkerHelper
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestWorkHelper @Inject constructor(val workerFactory: SaveTimeWorkerFactory): IWorkerHelper {
    override fun launchWorker(workerClass: Class<out ListenableWorker>, data: Data?, context: Context, name: String) {
        val uploadWorkRequest = TestListenableWorkerBuilder<SaveTimeWorker>(context)
            .setWorkerFactory(workerFactory)
            .build()

        runBlocking {
            uploadWorkRequest.doWork()
        }
    }
}