package com.croumy.hltb_wearos.presentation.ui.home.search

import SearchRequest
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.models.api.GameInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val hltbService: HLTBService,
) : ViewModel() {
    val searchRequest = mutableStateOf(SearchRequest())
    val searchText = mutableStateOf("")

    val isSearching = mutableStateOf(false)
    val canLoadMore = mutableStateOf(true)
    val resultGames = mutableStateOf(emptyList<GameInfo>())

    fun search(isPagination: Boolean = false) {
        viewModelScope.launch {
            isSearching.value = true
            searchRequest.value = searchRequest.value.copy(
                searchTerms = listOf(searchText.value),
                searchPage = if (isPagination) searchRequest.value.searchPage + 1 else 1
            )

            val result = hltbService.searchGame(searchRequest.value)?.games ?: emptyList()

            resultGames.value = if (isPagination) resultGames.value.plus(result) else result
            canLoadMore.value = result.size == searchRequest.value.size
            isSearching.value = false
        }
    }
}