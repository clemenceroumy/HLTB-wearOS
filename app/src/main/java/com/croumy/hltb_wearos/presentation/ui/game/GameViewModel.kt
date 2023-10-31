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
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.data.interfaces.IAppService
import com.croumy.hltb_wearos.presentation.data.PreferencesService
import com.croumy.hltb_wearos.presentation.data.interfaces.IHLTBService
import com.croumy.hltb_wearos.presentation.data.interfaces.IPreferenceService
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.models.Category
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.navigation.NavRoutes
import com.croumy.hltb_wearos.presentation.services.TimerService
import com.croumy.hltb_wearos.presentation.workers.SaveTimeWorker
import com.croumy.hltb_wearos.presentation.workers.WorkerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    val appService: IAppService,
    private val hltbService: IHLTBService,
    private val preferencesService: IPreferenceService,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context,
) : ViewModel() {
   private val id: Int = savedStateHandle.get<Int>(NavRoutes.GameDetails.ID) ?: 0

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
    val isInPlayingList: MutableState<Boolean> = mutableStateOf(false)
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

    suspend fun getGame(isRefresh: Boolean = false) {
        isLoading.value = true
        val result = hltbService.getGames()
        game.value = result.firstOrNull { it.id == id }
        if(!isRefresh) isInPlayingList.value = game.value?.categories?.contains(Category.Playing) ?: false
        isLoading.value = false
    }

    fun startTimer() {
        appService.timer.value = appService.timer.value.copy(
            gameId = game.value?.game_id,
            id = game.value?.id,
        )
        foregroundOnlyTimerService?.startTimer()
    }

    fun pauseTimer() {
        appService.pauseTimer()
    }

    fun stopTimer() {
        foregroundOnlyTimerService?.stopTimer()
    }

    fun cancelTimer() {
        appService.clearTimer()
    }

     fun saveTimer() {
        appService.timer.value = appService.timer.value.copy(state = TimerState.SAVING)
        appService.submitRequest.value = SubmitRequest(
            submissionId = game.value!!.id,
            userId = preferencesService.userId!!,
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

        WorkerHelper.launchWorker<SaveTimeWorker>(context = context, name = "saveTime")
    }
}