package com.croumy.hltb_wearos.presentation.ui.game

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.api.Categories
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.models.api.GameRequest
import com.croumy.hltb_wearos.presentation.navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale.Category
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle
): ViewModel() {
    val gameId: Int = savedStateHandle.get<Int>(NavRoutes.GameDetails.GAME_ID) ?: 0

    private val hltbService = HLTBService()

    val game: MutableState<Game?> = mutableStateOf(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        viewModelScope.launch { getGame() }
    }

    suspend fun getGame() {
        isLoading.value = true
        val result = hltbService.getGames(GameRequest().copy(lists = Categories.values().map { it.value }))
        game.value = result?.data?.gamesList?.firstOrNull { it.id == gameId }
        isLoading.value = false
    }
}