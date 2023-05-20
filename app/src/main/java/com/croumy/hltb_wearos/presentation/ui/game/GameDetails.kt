package com.croumy.hltb_wearos.presentation.ui.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.croumy.hltb_wearos.presentation.helpers.asString
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.red
import com.croumy.hltb_wearos.presentation.ui.game.components.LaunchButtons
import com.croumy.hltb_wearos.presentation.ui.game.components.LoadingGame
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GameDetails(
    viewModel: GameViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val game = viewModel.game.value
    val timer = viewModel.timer.value

    val progressAnimation = animateFloatAsState(timer.progress, label = "")

    Scaffold(
        modifier = Modifier.clip(CircleShape),
        timeText = { TimeText() }
    ) {
        Box {
            CircularProgressIndicator(
                progress = progressAnimation.value,
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
                if (game == null && viewModel.isLoading.value) {
                    LoadingGame()
                } else if (game == null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Can't retrieve game")
                    }
                } else {
                    //PLATFORM
                    Text(game.platform, style = MaterialTheme.typography.body2)
                    // GAME NAME
                    Text(
                        game.custom_title,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    // TIME INFO
                    AnimatedContent(targetState = timer.state, label = "") {
                        when (it) {
                            TimerState.IDLE -> Text(game.timePlayed.asString(withStringUnit = true), style = MaterialTheme.typography.title1)
                            TimerState.STARTED, TimerState.PAUSED -> Text(timer.time.asString(withSeconds = true, withZeros = false, withStringUnit = false), style = MaterialTheme.typography.title1)
                            TimerState.STOPPED, TimerState.SAVING -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(game.timePlayed.asString(withStringUnit = true), style = MaterialTheme.typography.body2)
                                Text("+${timer.time.asString(withSeconds = true, withZeros = false, withStringUnit = true)}", style = MaterialTheme.typography.title1)
                            }

                            TimerState.SAVED -> {}
                        }
                    }
                    // BUTTONS
                    LaunchButtons(
                        timer = timer,
                        startTimer = { viewModel.startTimer() },
                        pauseTimer = { viewModel.pauseTimer() },
                        stopTimer = { viewModel.stopTimer() },
                        cancelTimer = { viewModel.cancelTimer() },
                        saveTimer = {
                            coroutineScope.launch {
                                viewModel.saveTimer()
                            }
                        }
                    )
                }
            }
        }
    }
}