package com.croumy.hltb_wearos.presentation.ui.home.search

import SearchRequest
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material.Text
import com.croumy.hltb_wearos.presentation.models.api.GameInfo
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.ui.components.SearchGameItem
import com.croumy.hltb_wearos.presentation.ui.home.search.components.SearchButton
import com.croumy.hltbwearos.R
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    isFocusedScreen: Boolean = false,
    listState: ScalingLazyListState,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    navigateToAddGame: (GameInfo) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val firstTimeLoading = remember { mutableStateOf(true) }
    val hasNavigateToAddGame = remember { mutableStateOf(false) }

    LaunchedEffect(isFocusedScreen) {
        if (isFocusedScreen) {
            if(hasNavigateToAddGame.value) hasNavigateToAddGame.value = false
            else viewModel.search()
        } else {
            viewModel.searchRequest.value = SearchRequest()
            viewModel.searchText.value = ""
            viewModel.isSearching.value = false
            viewModel.resultGames.value = emptyList()
        }
    }

    LaunchedEffect(viewModel.resultGames.value) {
        if(viewModel.resultGames.value.isNotEmpty()) { focusRequester.requestFocus() }
    }

    Box(
        modifier = modifier.fillMaxHeight()
    ) {
        if (viewModel.isSearching.value && firstTimeLoading.value) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = Dimensions.xsStrokeSize,
                modifier = Modifier
                    .padding(Dimensions.xsPadding)
                    .size(Dimensions.sIcon)
                    .align(Alignment.Center)
            )
        } else Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchButton(onSearchResult = {
                viewModel.searchText.value = it
                coroutineScope.launch {
                    firstTimeLoading.value = true
                    viewModel.search()
                    listState.scrollToItem(0)
                }
            })
            Spacer(Modifier.height(Dimensions.xsPadding))
            if(viewModel.resultGames.value.isEmpty() && !viewModel.isSearching.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimensions.xsPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.search_no_results, "\"${viewModel.searchText.value}\""),
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.align(Alignment.Center).offset(y = (-Dimensions.sPadding))
                    )
                }
            } else ScalingLazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .onRotaryScrollEvent {
                        coroutineScope.launch {
                            listState.scrollBy(it.verticalScrollPixels)
                        }
                        true
                    }
                    .focusRequester(focusRequester)
                    .focusable(),
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                itemsIndexed(viewModel.resultGames.value) { index, game ->
                    SearchGameItem(
                        game,
                        onAddClick = {
                            navigateToAddGame(it)
                            hasNavigateToAddGame.value = true
                        }
                    )
                }

                if (viewModel.isSearching.value) item {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = Dimensions.xsStrokeSize,
                        modifier = Modifier
                            .padding(Dimensions.xsPadding)
                            .size(Dimensions.xsIcon)
                    )
                }
                item {
                    LaunchedEffect(true) {
                        if(viewModel.resultGames.value.isNotEmpty() && !viewModel.isSearching.value && viewModel.canLoadMore.value) {
                            firstTimeLoading.value = false
                            viewModel.search(isPagination = true)
                        }
                    }
                }
            }
        }
    }
}