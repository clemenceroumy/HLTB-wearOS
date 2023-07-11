package com.croumy.hltb_wearos.presentation.ui.game

import General
import Lists
import Progress
import SubmitRequest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.croumy.hltb_wearos.BuildConfig
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.Timer
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.models.api.Categories
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.models.api.GameRequest
import com.croumy.hltb_wearos.presentation.navigation.NavRoutes
import com.croumy.hltb_wearos.presentation.services.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    val appService: AppService,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context,
) : ViewModel() {
   private val gameId: Int = savedStateHandle.get<Int>(NavRoutes.GameDetails.GAME_ID) ?: 0

    private val hltbService = HLTBService()

    var foregroundOnlyServiceBound = false
    private var foregroundOnlyTimerService: TimerService? = null
    private val foregroundOnlyServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as TimerService.LocalBinder
            foregroundOnlyTimerService = binder.timerService
            foregroundOnlyServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyTimerService = null
            foregroundOnlyServiceBound = false
        }
    }

    val game: MutableState<Game?> = mutableStateOf(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        viewModelScope.launch { getGame() }
    }

    fun bindService() {
        val serviceIntent = Intent(context, TimerService::class.java)
        context.applicationContext.bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService() {
        if (foregroundOnlyServiceBound) {
            context.unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyServiceBound = false
        }
    }

    private suspend fun getGame() {
        isLoading.value = true
        val result = hltbService.getGames(GameRequest().copy(lists = Categories.values().map { it.value }))
        game.value = result?.data?.gamesList?.firstOrNull { it.game_id == gameId }
        appService.timer.value = appService.timer.value.copy(gameId = game.value?.game_id)
        isLoading.value = false
    }

    fun startTimer() {
        foregroundOnlyTimerService?.startTimer()
    }

    fun pauseTimer() {
        appService.timer.value = appService.timer.value.copy(state = TimerState.PAUSED)
    }

    fun stopTimer() {
        foregroundOnlyTimerService?.stopTimer()
    }

    fun cancelTimer() {
        appService.timer.value = Timer()
    }

    suspend fun saveTimer() {
        appService.timer.value = appService.timer.value.copy(state = TimerState.SAVING)
        val body = SubmitRequest(
            submissionId = game.value!!.id,
            userId = BuildConfig.USER_ID.toInt(), // TODO: Get from HLTB
            gameId = game.value!!.game_id,
            title = game.value!!.custom_title,
            platform = game.value!!.platform,
            storefront = game.value!!.play_storefront,
            lists = Lists(),
            general = General(
                progress = Progress.progressTime(game.value!!.timePlayed, appService.timer.value.time),
                progressBefore = Progress.fromTime(game.value!!.timePlayed)
            )
        )

        hltbService.submitTime(body)
        appService.timer.value = appService.timer.value.copy(state = TimerState.SAVED)

        //RESET TIMER AND REFRESH GAME
        appService.timer.value = Timer()
        this.getGame()
    }
}