package com.croumy.hltb_wearos.presentation.services


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import com.croumy.hltb_wearos.BuildConfig
import com.croumy.hltb_wearos.R
import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.helpers.Constants
import com.croumy.hltb_wearos.presentation.helpers.Constants.Companion.NOTIFICATION_ID
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.navigation.NavRoutes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


const val CODE_FOREGROUND_SERVICE = 1

@Singleton
@AndroidEntryPoint
class TimerService : LifecycleService() {
    @Inject
    lateinit var appService: AppService

    private lateinit var notificationManager: NotificationManager
    private val localBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        internal val timerService: TimerService get() = this@TimerService
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)

        notForegroundService()
        return localBinder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        notForegroundService()
    }

    override fun onUnbind(intent: Intent): Boolean {
        val notification = generateNotification("Running")
        startForeground(NOTIFICATION_ID, notification)

        return true
    }

    private fun notForegroundService() {
        stopForeground(true)
    }

    fun startTimer() {
        val intent = Intent(this, TimerService::class.java)
        startService(intent)
        lifecycleScope.launch {
            appService.startTimer()
        }
    }

    fun stopTimer() {
        lifecycleScope.launch {
            appService.timer.value = appService.timer.value.copy(state = TimerState.STOPPED)
        }
    }

    private fun generateNotification(mainText: String): Notification {
        val notificationChannel = NotificationChannel(Constants.CHANNEL_ID, "TimerChannel", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(notificationChannel)

        val route = NavRoutes.GameDetails.routeWithArgs
            .replace("{${NavRoutes.GameDetails.GAME_ID}}", appService.timer.value.gameId.toString())

        val intent = Intent(
            Intent.ACTION_VIEW,
            "app://${BuildConfig.APPLICATION_ID}/${route}".toUri(),
            this,
            TimerService::class.java
        )
        val activityPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, Constants.CHANNEL_ID)

        val notificationBuilder = notificationCompatBuilder
            .setContentText(mainText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_STOPWATCH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)


        val ongoingActivityStatus = Status.Builder()
            .addTemplate(mainText)
            .build()

        val ongoingActivity =
            OngoingActivity.Builder(applicationContext, NOTIFICATION_ID, notificationBuilder)
                .setStaticIcon(R.drawable.logo_notification)
                .setTouchIntent(activityPendingIntent)
                .setStatus(ongoingActivityStatus)
                .build()

        ongoingActivity.apply(applicationContext)

        return notificationBuilder.build()
    }

    /*private fun startForegroundNotification() {
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
    }*/
}