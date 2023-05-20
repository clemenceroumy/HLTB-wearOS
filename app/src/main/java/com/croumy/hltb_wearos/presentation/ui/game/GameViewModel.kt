package com.croumy.hltb_wearos.presentation.ui.game

import General
import Lists
import Progress
import SubmitRequest
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.Timer
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.models.api.Categories
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.models.api.GameRequest
import com.croumy.hltb_wearos.presentation.navigation.NavRoutes
import com.soywiz.klock.milliseconds
import com.soywiz.klock.plus
import com.soywiz.klock.seconds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale.Category
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val gameId: Int = savedStateHandle.get<Int>(NavRoutes.GameDetails.GAME_ID) ?: 0

    private val hltbService = HLTBService()

    val game: MutableState<Game?> = mutableStateOf(null)
    val timer = mutableStateOf(Timer())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        viewModelScope.launch { getGame() }
    }

    suspend fun getGame() {
        isLoading.value = true
        timer.value = Timer()
        val result = hltbService.getGames(GameRequest().copy(lists = Categories.values().map { it.value }))
        game.value = result?.data?.gamesList?.firstOrNull { it.game_id == gameId }
        isLoading.value = false
    }

    fun startTimer() {
        viewModelScope.launch {
            timer.value = timer.value.copy(state = TimerState.STARTED)
            while (timer.value.state == TimerState.STARTED) {
                delay(10)
                timer.value = timer.value.copy(time = timer.value.time.plus(10.milliseconds))
            }
        }
    }

    fun pauseTimer() {
        timer.value = timer.value.copy(state = TimerState.PAUSED)
    }

    fun stopTimer() {
        timer.value = timer.value.copy(state = TimerState.STOPPED)
    }

    fun cancelTimer() {
        timer.value = Timer()
    }

    suspend fun saveTimer() {
        timer.value = timer.value.copy(state = TimerState.SAVING)
        val body = SubmitRequest(
            submissionId = game.value!!.id,
            userId = 304670, // TODO: Get from HLTB
            gameId = game.value!!.game_id,
            title = game.value!!.custom_title,
            platform = game.value!!.platform,
            storefront = game.value!!.play_storefront,
            lists = Lists(),
            general = General(
                progress = Progress.progressTime(game.value!!.timePlayed, timer.value.time),
                progressBefore = Progress.fromTime(game.value!!.timePlayed)
            )
        )
        print(body)
        hltbService.submitTime(body)
        timer.value = timer.value.copy(state = TimerState.SAVED)
        this.getGame()
    }
}