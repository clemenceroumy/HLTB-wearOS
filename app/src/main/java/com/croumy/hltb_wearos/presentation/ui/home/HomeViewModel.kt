package com.croumy.hltb_wearos.presentation.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.api.Categories
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.models.api.GameRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val appService: AppService,
    private val hltbService: HLTBService
): ViewModel() {
    private val games: MutableState<List<Game>> = mutableStateOf(emptyList())
    val gamesByCategories get(): Map<Categories, List<Game>> {
        val map = mutableMapOf<Categories, List<Game>>()
        Categories.values().forEach { category ->
            map[category] = games.value.filter { it.categories.contains(category) }
        }
        return map
    }
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        viewModelScope.launch { getGames() }
    }

    private suspend fun getGames() {
        isLoading.value = true
        val result = hltbService.getGames()
        games.value = (result?.data?.gamesList ?: emptyList()).sortedBy { it.invested_pro }.reversed()
        isLoading.value = false
    }
}