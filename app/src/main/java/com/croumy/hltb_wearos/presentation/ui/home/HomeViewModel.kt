package com.croumy.hltb_wearos.presentation.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.api.Category
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.models.api.categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val appService: AppService,
    private val hltbService: HLTBService
): ViewModel() {
    private val games: MutableState<List<Game>> = mutableStateOf(emptyList())
    // IN VIEWMODEL TO KEEP LIST SCROLLSTATE ON NAVIGATION
    val listStates = listOf(ScalingLazyListState(initialCenterItemIndex = 0), ScalingLazyListState(initialCenterItemIndex = 0)).plus((categories).map { ScalingLazyListState(initialCenterItemIndex = 0) })
    val currentListState = mutableStateOf(listStates[1])

    val gamesByCategories get(): Map<Category, List<Game>> {
        val map = mutableMapOf<Category, List<Game>>()
        categories.forEach { category ->
            map[category] = games.value.filter { it.categories.contains(category) }
        }
        return map
    }
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        viewModelScope.launch { getGames() }
    }

    suspend fun getGames() {
        isLoading.value = true
        val result = hltbService.getGames()
        games.value = (result?.data?.gamesList ?: emptyList()).sortedBy { it.invested_pro }.reversed()
        isLoading.value = false
    }
}