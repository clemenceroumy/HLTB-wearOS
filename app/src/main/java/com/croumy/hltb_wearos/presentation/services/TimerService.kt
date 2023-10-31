package com.croumy.hltb_wearos.presentation.services


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.res.Configuration
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleService
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.interfaces.IAppService
import com.croumy.hltb_wearos.presentation.models.Constants
import com.croumy.hltb_wearos.presentation.models.Constants.Companion.NOTIFICATION_ID
import com.croumy.hltb_wearos.presentation.navigation.NavRoutes
import com.croumy.hltbwearos.BuildConfig
import com.croumy.hltbwearos.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton


const val CODE_FOREGROUND_SERVICE = 1

@Singleton
@AndroidEntryPoint
class TimerService : LifecycleService() {
    @Inject
    lateinit var appService: IAppService

    private lateinit var notificationManager: NotificationManager
    private lateinit var mWakeLock: WakeLock
    private var serviceRunningInForeground = false
    private var configurationChange = false
    private var timerActive = false
    private val localBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        internal val timerService: TimerService get() = this@TimerService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pm = getSystemService(POWER_SERVICE) as PowerManager
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "HLTB-wearOS::TimerService")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(TAG, "onStartCommand()")

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        Log.d(TAG, "onBind()")

        notForegroundService()
        return localBinder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind()")

        notForegroundService()
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "onUnbind()")

        if (!configurationChange && timerActive) {
            val notification = generateNotification("0")
            startForeground(NOTIFICATION_ID, notification)
            serviceRunningInForeground = true
        }

        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    private fun notForegroundService() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        serviceRunningInForeground = false
        configurationChange = false
    }

    fun startTimer() {
        Log.d(TAG, "startTimer()")
        timerActive = true
        mWakeLock.acquire()

        val intent = Intent(applicationContext, TimerService::class.java)
        startService(intent)

        appService.startTimer()
        if (serviceRunningInForeground) {
            val notification = generateNotification(appService.timer.value.time.toString())
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    fun stopTimer() {
        Log.d(TAG, "stopTimer()")
        timerActive = false
        mWakeLock.release()

        appService.stopTimer()
    }

    private fun generateNotification(text: String): Notification {
        val notificationChannel = NotificationChannel(Constants.CHANNEL_ID, "TimerChannel", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(notificationChannel)

        val route = NavRoutes.GameDetails.routeWithArgs.replace("{${NavRoutes.GameDetails.ID}}", appService.timer.value.id.toString())

        val intent = Intent(
            Intent.ACTION_VIEW,
            "app://${BuildConfig.APPLICATION_ID}/${route}".toUri(),
            applicationContext,
            MainActivity::class.java
        ).apply {
            flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        }

        val activityPendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)

        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, Constants.CHANNEL_ID)

        val notificationBuilder = notificationCompatBuilder
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_STOPWATCH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)


        val ongoingActivityStatus = Status.Builder()
            .addTemplate(text)
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

    companion object {
        const val TAG: String = "TimerService"
    }
}