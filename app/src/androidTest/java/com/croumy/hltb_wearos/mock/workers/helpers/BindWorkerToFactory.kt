package com.croumy.hltb_wearos.mock.workers.helpers

import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import com.croumy.hltb_wearos.mock.workers.SaveTimeWorkerFactory
import com.croumy.hltb_wearos.presentation.workers.SaveTimeWorker
import javax.inject.Inject

class BindWorkerToFactory @Inject constructor(
    saveTimeWorkerFactory: SaveTimeWorkerFactory
) {
    private val bindObject: Map<Class<out ListenableWorker>, WorkerFactory> = mapOf(
        SaveTimeWorker::class.java to saveTimeWorkerFactory
    )

    fun bind(workerClass: Class<out ListenableWorker>): WorkerFactory {
        return bindObject[workerClass]!!
    }
}