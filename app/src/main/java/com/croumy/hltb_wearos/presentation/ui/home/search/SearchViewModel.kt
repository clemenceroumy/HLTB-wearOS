package com.croumy.hltb_wearos.presentation.ui.home.search

import SearchRequest
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.data.PreferencesService
import com.croumy.hltb_wearos.presentation.models.api.GameInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val hltbService: HLTBService,
): ViewModel() {
    val searchRequest = mutableStateOf(SearchRequest())
    val searchText = mutableStateOf("")

    val isSearching = mutableStateOf(false)
    val resultGames = mutableStateOf(emptyList<GameInfo>())

    suspend fun search() {
        isSearching.value = true
        searchRequest.value = searchRequest.value.copy(
            searchTerms = listOf(searchText.value)
        )
        resultGames.value = hltbService.searchGame(searchRequest.value)?.games ?: emptyList()
        isSearching.value = false
    }
}