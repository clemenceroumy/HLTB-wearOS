package com.croumy.hltb_wearos.presentation.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.Category
import com.croumy.hltb_wearos.presentation.models.api.Game
import com.croumy.hltb_wearos.presentation.models.categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val appService: AppService,
    private val hltbService: HLTBService
) : ViewModel() {
    private val _games = MutableStateFlow<List<Game>>(emptyList())

    // IN VIEWMODEL TO KEEP LIST SCROLLSTATE ON NAVIGATION
    val listStates by lazy {
        listOf(ScalingLazyListState(initialCenterItemIndex = 0), ScalingLazyListState(initialCenterItemIndex = 0)).plus((categories).map { ScalingLazyListState(initialCenterItemIndex = 0) })
    }
    val currentListState = mutableStateOf(listStates[2])

    val gamesByCategories = mutableStateOf<Map<Category, List<Game>>>(emptyMap())

    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        viewModelScope.launch { getGames() }
    }

    fun getGames() {
        viewModelScope.launch {
            isLoading.value = true
            _games.value = hltbService.getGames()
            isLoading.value = false
            updateGamesByCategories()
        }
    }

    private fun updateGamesByCategories() {
        val map = mutableMapOf<Category, List<Game>>()
        categories.forEach { category ->
            map[category] = _games.value.filter { it.categories.contains(category) }.sortedWith(
                when (category) {
                    Category.Playing -> compareByDescending { it.invested_pro }
                    Category.Backlog, Category.Custom, Category.Custom2, Category.Custom3, Category.Retired -> compareBy { it.custom_title }
                    Category.Completed -> compareByDescending { it.completedDate }
                    else -> { compareBy { it.custom_title } }
                }
            )
        }
        gamesByCategories.value = map
    }
}