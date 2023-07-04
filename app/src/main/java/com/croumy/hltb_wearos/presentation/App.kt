package com.croumy.hltb_wearos.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.croumy.hltb_wearos.presentation.helpers.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    private fun createChannel() {
        /*val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(Constants.CHANNEL_ID, "TimerChannel", NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)*/
    }

}
