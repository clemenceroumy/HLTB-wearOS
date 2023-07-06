package com.croumy.hltb_wearos.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
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

    val categories = Categories.values().sortedArray()

    val focusRequester = remember { (0..categories.size).map { FocusRequester() }}
    val listState = remember { mutableStateOf((0..categories.size).map { ScalingLazyListState(initialCenterItemIndex = 0) }) }
    val pagerState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = -0.5f) { categories.size }


    LaunchedEffect(pagerState.currentPage) {
        Log.i("HOME", "page changed to ${pagerState.currentPage}")
        viewModel.currentCategory.value = categories[pagerState.currentPage]
        focusRequester[pagerState.currentPage].requestFocus()

        listState.value[pagerState.currentPage].scrollToItem(0)
    }

    Scaffold(
        positionIndicator = { PositionIndicator(scalingLazyListState = listState.value[pagerState.currentPage]) },
        timeText = { TimeText() }
    ) {
        /*HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            key = { categories[it] }
        ) {*/
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(Dimensions.mPadding))
                Row(
                    Modifier
                        .padding(bottom = Dimensions.xxsPadding)
                        .background(viewModel.currentCategory.value.color, CircleShape)
                        .padding(horizontal = Dimensions.sPadding, vertical = Dimensions.xxsPadding),
                ) {
                    Text(viewModel.currentCategory.value.label)
                }
                ScalingLazyColumn(
                    state = listState.value[pagerState.currentPage],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimensions.xxsPadding)
                        .onRotaryScrollEvent {
                            coroutineScope.launch {
                                listState.value[pagerState.currentPage].scrollBy(it.verticalScrollPixels)
                            }
                            true
                        }
                        .focusRequester(focusRequester[pagerState.currentPage])
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
                        items(viewModel.gamesByCategory) { game ->
                            GameItem(
                                game,
                                modifier = Modifier.clickable { navigateToGame(game.game_id) }
                            )
                        }
                    }
                }
            }
        }
    //}
}

@Preview
@Composable
fun HomePreview() {
    HLTBwearosTheme {
        HomeScreen()
    }
}