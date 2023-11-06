package com.croumy.hltb_wearos.mock.workers.helpers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.await
import androidx.work.testing.TestListenableWorkerBuilder
import com.croumy.hltb_wearos.mock.workers.SaveTimeWorkerFactory
import com.croumy.hltb_wearos.presentation.workers.interfaces.IWorkerHelper
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestWorkHelper @Inject constructor(private val bindWorkerToFactory: BindWorkerToFactory): IWorkerHelper {
    override fun launchWorker(workerClass: Class<out ListenableWorker>, data: Data?, context: Context, name: String) {
        val workerFactory = bindWorkerToFactory.bind(workerClass)
        val uploadWorkRequest = TestListenableWorkerBuilder.from(context, workerClass)
            .setWorkerFactory(workerFactory)
            .build()

        runBlocking {
            uploadWorkRequest.startWork().await()
        }
    }
}