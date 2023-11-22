package com.croumy.hltb_wearos.presentation.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.croumy.hltb_wearos.presentation.LocalNavController
import com.croumy.hltb_wearos.presentation.models.Constants
import com.croumy.hltb_wearos.presentation.models.Category
import com.croumy.hltb_wearos.presentation.models.api.GameInfo
import com.croumy.hltb_wearos.presentation.models.categories
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.HLTBwearosTheme
import com.croumy.hltb_wearos.presentation.theme.shimmerColor
import com.croumy.hltb_wearos.presentation.ui.components.GameItem
import com.croumy.hltb_wearos.presentation.ui.home.logs.LogsScreen
import com.croumy.hltb_wearos.presentation.ui.home.search.SearchScreen
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToGame: (Int) -> Unit = {},
    navigateToAddGame: (GameInfo) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val navController = LocalNavController.current

    val needRefresh = navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(Constants.HOME_NEED_REFRESH)
    val previousGameId = navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(Constants.HOME_PREVIOUS_GAME_ID)

    // CROWN SCROLL CONFIG + LISTS STATES
    val focusRequester = remember { listOf(FocusRequester(), FocusRequester()).plus((categories).map { FocusRequester() }) }
    val horizontalScrollState = rememberLazyListState(initialFirstVisibleItemIndex = 2)
    val horizontalFirstVisibleIndex = remember { derivedStateOf { horizontalScrollState.firstVisibleItemIndex } }

    LaunchedEffect(horizontalFirstVisibleIndex.value, viewModel.currentListState.value) {
        // ON CHANGE OF CATEGORY, RESET SCROLL OF PREVIOUS CATEGORY AND GO TO TOP OF LIST
        val hasHorizontallyScrolled = viewModel.listStates.indexOf(viewModel.currentListState.value) != horizontalFirstVisibleIndex.value
        if(hasHorizontallyScrolled) viewModel.currentListState.value.scrollToItem(0)

        // SET CURRENT SCREEN LIST SCROLL STATE
        viewModel.currentListState.value = viewModel.listStates[horizontalFirstVisibleIndex.value]

        if (horizontalFirstVisibleIndex.value > 1) focusRequester[horizontalFirstVisibleIndex.value].requestFocus()
    }

    LaunchedEffect(needRefresh?.value) {
        // IF PREVIOUS GAME WAS SAVED AND NOT ON THE PLAYING LIST,
        if (needRefresh?.value == true) {
            //REFRESH GAME LIST
            viewModel.getGames()
            navController.currentBackStackEntry?.savedStateHandle?.set(Constants.HOME_NEED_REFRESH, false)
            // SCROLL TO PLAYING LIST
            horizontalScrollState.scrollToItem(2)
            //AND SCROLL TO GAME ITEM
            val gameIndex = viewModel.gamesByCategories[Category.Playing]?.indexOfFirst { it.game_id == previousGameId?.value }
            viewModel.currentListState.value = viewModel.listStates[2]
            viewModel.currentListState.value.scrollToItem(gameIndex ?: 0)
            navController.currentBackStackEntry?.savedStateHandle?.set(Constants.HOME_PREVIOUS_GAME_ID, null)
        }
    }

    Scaffold(
        positionIndicator = { PositionIndicator(scalingLazyListState = viewModel.currentListState.value) },
        timeText = { TimeText() },
    ) {
        LazyRow(
            modifier = Modifier.padding(top = Dimensions.mPadding),
            state = horizontalScrollState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = horizontalScrollState)
        ) {
            item {
                SearchScreen(
                    modifier = Modifier.width(screenWidth.dp),
                    isFocusedScreen = horizontalFirstVisibleIndex.value == 0,
                    listState = viewModel.listStates[0],
                    focusRequester = focusRequester[0],
                    navigateToAddGame = navigateToAddGame,
                    onGameAdded = { coroutineScope.launch { viewModel.getGames() } }
                )
            }

            item {
                LogsScreen(
                    modifier = Modifier.width(screenWidth.dp),
                    isFocusedScreen = horizontalFirstVisibleIndex.value == 1,
                    listState = viewModel.listStates[1],
                    focusRequester = focusRequester[1],
                    refreshGames = { coroutineScope.launch { viewModel.getGames() } }
                )
            }

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
                            .padding(
                                horizontal = Dimensions.sPadding,
                                vertical = Dimensions.xxsPadding
                            ),
                    ) { Text(category.label ?: "") }

                    ScalingLazyColumn(
                        state = viewModel.listStates[index + 2],
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Dimensions.xxsPadding)
                            .onRotaryScrollEvent {
                                coroutineScope.launch {
                                    viewModel.listStates[index + 2].scrollBy(it.verticalScrollPixels)
                                }
                                true
                            }
                            .focusRequester(focusRequester[index + 2])
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
                                    isRunning = viewModel.appService.timer.value.id == game.id,
                                    onClick = { navigateToGame(game.id) }
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