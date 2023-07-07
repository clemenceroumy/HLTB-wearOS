package com.croumy.hltb_wearos.presentation.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.api.Categories
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.models.api.GameRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale.Category
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    private val hltbService = HLTBService()

    private val games: MutableState<List<Game>> = mutableStateOf(emptyList())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        viewModelScope.launch { getGames() }
    }

    fun gamesByCategory(category: Categories): List<Game> = games.value.filter { it.categories.contains(category) }

    private suspend fun getGames() {
        isLoading.value = true
        val result = hltbService.getGames(GameRequest())
        games.value = (result?.data?.gamesList ?: emptyList()).sortedBy { it.invested_pro }.reversed()
        isLoading.value = false
    }
}