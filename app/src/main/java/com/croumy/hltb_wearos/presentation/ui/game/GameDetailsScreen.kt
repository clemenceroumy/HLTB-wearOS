package com.croumy.hltb_wearos.presentation.ui.game

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.wear.compose.foundation.SwipeToDismissValue
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.croumy.hltb_wearos.presentation.LocalNavController
import com.croumy.hltb_wearos.presentation.LocalNavSwipeBox
import com.croumy.hltb_wearos.presentation.helpers.extensions.asString
import com.croumy.hltb_wearos.presentation.models.Constants
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.primary
import com.croumy.hltb_wearos.presentation.ui.game.components.LaunchButtons
import com.croumy.hltb_wearos.presentation.ui.game.components.LoadingGame
import com.croumy.hltbwearos.R
import kotlinx.coroutines.launch

@Composable
fun GameDetailsScreen(
    viewModel: GameViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val navController = LocalNavController.current
    val swipeBoxState = LocalNavSwipeBox.current

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val timer = viewModel.appService.timer.value
    val hasSavedSession = remember { mutableStateOf(false) }

    val progressAnimation = animateFloatAsState(timer.progress, label = "")
    val isActiveSession = timer.id == viewModel.game.value?.id || timer.id == null

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Log.i("Game", event.name)
            if (event == Lifecycle.Event.ON_START) {
                viewModel.bindService()
            }
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.unbindService()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(swipeBoxState.targetValue) {
        // ON BACK SWIPE, TELL HOME TO REFRESH (OR NOT) THE GAMES
        // IF GAME WAS NOT IN PLAYING LIST, DO REFRESH
        if(swipeBoxState.targetValue == SwipeToDismissValue.Dismissed) {
            val needRefresh = hasSavedSession.value && !viewModel.isInPlayingList.value

            navController.previousBackStackEntry
                ?.savedStateHandle?.apply {
                    set(Constants.HOME_NEED_REFRESH, needRefresh)
                    set(Constants.HOME_PREVIOUS_GAME_ID, viewModel.game.value?.game_id)
                }
        }
    }

    LaunchedEffect(viewModel.appService.timer.value.state) {
        if(viewModel.appService.timer.value.state == TimerState.SAVING) hasSavedSession.value = true
        if (viewModel.appService.timer.value.state == TimerState.SAVED) {
            viewModel.appService.clearTimer()
            coroutineScope.launch { viewModel.getGame(isRefresh = true) }
        }
        else if (viewModel.appService.timer.value.state == TimerState.ERROR) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message = context.resources.getString(R.string.session_error_upload))
            }
            viewModel.appService.clearTimer()
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.clip(CircleShape),
            timeText = { TimeText() }
        ) {
            Box {
                CircularProgressIndicator(
                    progress = if (isActiveSession) progressAnimation.value else 0f,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimensions.xxsPadding),
                    strokeWidth = Dimensions.xxsSize,
                    indicatorColor = MaterialTheme.colors.secondary,
                    startAngle = -60f,
                    endAngle = 240f,
                )

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(Dimensions.xxsPadding + Dimensions.xxsSize)
                        .background(Color.Transparent, CircleShape)
                        .padding(Dimensions.sPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    if (viewModel.game.value == null && viewModel.isLoading.value) {
                        LoadingGame()
                    } else if (viewModel.game.value == null) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            stringResource(R.string.session_error_game)
                        }
                    } else {
                        //PLATFORM
                        Text(
                            viewModel.game.value!!.platform,
                            style = MaterialTheme.typography.body2
                        )
                        // GAME NAME
                        Text(
                            viewModel.game.value!!.custom_title,
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                        // TIME INFO
                        // IF TIMER IS RUNNING AND SELECTED GAME IS RUNNING GAME OR TIMER IS NOT RUNNING
                        if (isActiveSession) {
                            when (timer.state) {
                                TimerState.IDLE -> Text(
                                    viewModel.game.value!!.timePlayed.asString(
                                        withStringUnit = true
                                    ), style = MaterialTheme.typography.title1
                                )

                                TimerState.STARTED, TimerState.PAUSED -> Text(
                                    timer.time.asString(
                                        withSeconds = true,
                                        withZeros = false,
                                        withStringUnit = false
                                    ), style = MaterialTheme.typography.title1
                                )

                                TimerState.STOPPED, TimerState.SAVING -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        viewModel.game.value!!.timePlayed.asString(withStringUnit = true),
                                        style = MaterialTheme.typography.body2,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        "+${
                                            timer.time.asString(
                                                withSeconds = true,
                                                withZeros = false,
                                                withStringUnit = true
                                            )
                                        }",
                                        style = MaterialTheme.typography.title2,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                TimerState.SAVED, TimerState.ERROR -> {}
                            }
                            // BUTTONS
                            LaunchButtons(
                                timer = timer,
                                startTimer = { viewModel.startTimer() },
                                pauseTimer = { viewModel.pauseTimer() },
                                stopTimer = { viewModel.stopTimer() },
                                cancelTimer = { viewModel.cancelTimer() },
                                saveTimer = { coroutineScope.launch { viewModel.saveTimer() } }
                            )
                        }
                        // IF SELECTED GAME IS OTHER GAME THAN LAUNCHED GAME
                        else {
                            Text(
                                viewModel.game.value!!.timePlayed.asString(withStringUnit = true),
                                style = MaterialTheme.typography.title1
                            )
                            Box(
                                Modifier
                                    .background(primary, CircleShape)
                                    .padding(Dimensions.xsPadding),
                            ) {
                                Text(
                                    stringResource(id = R.string.session_already_launched),
                                    style = MaterialTheme.typography.body2,
                                    color = Color.White
                                )
                            }
                        }

                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = Dimensions.sPadding),
            snackbar = {
                Box(
                    Modifier
                        .background(
                            MaterialTheme.colors.surface,
                            CircleShape
                        )
                        .padding(Dimensions.xsPadding)
                ) {
                    Text(
                        "${snackbarHostState.currentSnackbarData?.message}",
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        )
    }
}