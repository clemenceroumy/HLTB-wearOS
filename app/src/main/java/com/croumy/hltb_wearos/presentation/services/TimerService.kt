package com.croumy.hltb_wearos.presentation.services


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleService
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import com.croumy.hltb_wearos.BuildConfig
import com.croumy.hltb_wearos.R
import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.helpers.Constants
import com.croumy.hltb_wearos.presentation.helpers.Constants.Companion.NOTIFICATION_ID
import com.croumy.hltb_wearos.presentation.models.Timer
import com.croumy.hltb_wearos.presentation.navigation.NavRoutes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


const val CODE_FOREGROUND_SERVICE = 1

@Singleton
@AndroidEntryPoint
class TimerService : LifecycleService() {
    @Inject
    lateinit var appService: AppService

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        //val pm = getSystemService(POWER_SERVICE) as PowerManager
        //val mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "systemService")

        MainScope().launch {
            startForegroundNotification()
            appService.startTimer()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        MainScope().launch {
            appService.timer.value = Timer()
        }
    }

    private fun startForegroundNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID).apply {
            clearActions()
            setSmallIcon(R.drawable.logo_notification)
            setAutoCancel(false)
            setOngoing(true)
            setSilent(true)
        }

        val ongoingActivityStatus = Status.Builder()
            .build()

        val route = NavRoutes.GameDetails.routeWithArgs
            .replace("{${NavRoutes.GameDetails.GAME_ID}}", appService.timer.value.gameId.toString())

        val intent = Intent(
            Intent.ACTION_VIEW,
            "app://${BuildConfig.APPLICATION_ID}/${route}".toUri(),
            this,
            MainActivity::class.java
        )

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val ongoingActivity =
            OngoingActivity.Builder(applicationContext, NOTIFICATION_ID, builder)
                .setStaticIcon(R.drawable.logo_notification)
                .setTouchIntent(pendingIntent)
                .setStatus(ongoingActivityStatus)
                .build()

        ongoingActivity.apply(applicationContext)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
        startForeground(CODE_FOREGROUND_SERVICE, builder.build())
    }
}