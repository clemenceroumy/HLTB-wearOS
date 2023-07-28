package com.croumy.hltb_wearos.presentation.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.croumy.hltb_wearos.presentation.components.GameItem
import com.croumy.hltb_wearos.presentation.models.api.Categories
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.HLTBwearosTheme
import com.croumy.hltb_wearos.presentation.theme.shimmerColor
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToGame: (Int) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp

    val categories = Categories.values().sortedArray()

    val focusRequester = (categories).map { FocusRequester() }
    val horizontalScrollState = rememberLazyListState()
    val horizontalFirstVisibleIndex = remember { derivedStateOf { horizontalScrollState.firstVisibleItemIndex } }
    val listStates = (categories).map { ScalingLazyListState(initialCenterItemIndex = 0) }
    val currentListState = remember { mutableStateOf(listStates[0]) }

    LaunchedEffect(horizontalFirstVisibleIndex.value) {
        focusRequester[horizontalFirstVisibleIndex.value].requestFocus()
        currentListState.value = listStates[horizontalFirstVisibleIndex.value]
    }

    Scaffold(
        positionIndicator = { PositionIndicator(scalingLazyListState = currentListState.value) },
        timeText = { TimeText() }
    ) {
        LazyRow(
            modifier = Modifier.padding(top = Dimensions.mPadding),
            state = horizontalScrollState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = horizontalScrollState)
        ) {
            itemsIndexed(categories) { index, category ->
                val games = viewModel.gamesByCategories[category] ?: emptyList()

                Column(
                   Modifier.width(screenWidth.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier
                            .padding(bottom = Dimensions.xxsPadding)
                            .background(category.color, CircleShape)
                            .padding(horizontal = Dimensions.sPadding, vertical = Dimensions.xxsPadding),
                    ) { Text(category.label) }

                    ScalingLazyColumn(
                        state = listStates[index],
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Dimensions.xxsPadding)
                            .onRotaryScrollEvent {
                                coroutineScope.launch {
                                    listStates[index].scrollBy(it.verticalScrollPixels)
                                }
                                true
                            }
                            .focusRequester(focusRequester[index])
                            .focusable(),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.xsPadding),
                        contentPadding = PaddingValues(bottom = Dimensions.xsPadding),
                    ) {
                        if (viewModel.isLoading.value) {
                            items(2) {
                                Box(
                                    modifier = Modifier
                                        .shimmer()
                                        .height(Dimensions.lSize)
                                        .fillMaxWidth()
                                        .background(shimmerColor, CircleShape)
                                )
                            }
                        } else {
                            items(games) { game ->
                                GameItem(
                                    game,
                                    isRunning = viewModel.appService.timer.value.gameId == game.game_id,
                                    onClick = { navigateToGame(game.game_id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    HLTBwearosTheme {
        HomeScreen()
    }
}