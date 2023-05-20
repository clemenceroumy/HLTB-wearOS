package com.croumy.hltb_wearos.presentation.ui.game.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.wear.compose.material.MaterialTheme
import com.croumy.hltb_wearos.presentation.models.Timer
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.Dimensions.Companion.lIcon
import com.croumy.hltb_wearos.presentation.theme.Dimensions.Companion.mIcon
import com.croumy.hltb_wearos.presentation.theme.Dimensions.Companion.sIcon
import com.croumy.hltb_wearos.presentation.theme.red

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LaunchButtons(
    timer: Timer,
    startTimer: () -> Unit,
    pauseTimer: () -> Unit,
    stopTimer: () -> Unit,
    cancelTimer: () -> Unit,
    saveTimer: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (timer.state == TimerState.STOPPED || timer.state == TimerState.SAVING) {
            Box(
                Modifier
                    .size(sIcon)
                    .background(MaterialTheme.colors.surface, CircleShape)
                    .clip(CircleShape)
                    .clickable { if (timer.state != TimerState.SAVING) cancelTimer() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(Dimensions.xxsPadding)
                        .size(sIcon),
                    tint = Color.White
                )
            }

            Box(
                Modifier
                    .offset(x = Dimensions.xsPadding)
                    .size(mIcon)
                    .background(MaterialTheme.colors.primary, CircleShape)
                    .clip(CircleShape)
                    .clickable { if (timer.state != TimerState.SAVING) saveTimer() },
                contentAlignment = Alignment.Center
            ) {
                if (timer.state == TimerState.STOPPED) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(Dimensions.xxsPadding)
                            .size(mIcon),
                        tint = Color.White
                    )
                } else if (timer.state == TimerState.SAVING) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(Dimensions.xsPadding)
                            .size(mIcon),
                        color = Color.White,
                        strokeWidth = Dimensions.xsStrokeSize,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        } else {
            Box(
                Modifier
                    .size(mIcon)
                    .background(MaterialTheme.colors.primary, CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        if (timer.state == TimerState.IDLE || timer.state == TimerState.PAUSED) {
                            startTimer()
                        } else if (timer.state == TimerState.STARTED) {
                            pauseTimer()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(targetState = (timer.state == TimerState.IDLE || timer.state == TimerState.PAUSED), label = "") { isInactive ->
                    if (isInactive) {
                        Icon(
                            Icons.Filled.PlayArrow,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(Dimensions.xxsPadding)
                                .size(mIcon),
                            tint = Color.White
                        )
                    } else {
                        Icon(
                            Icons.Filled.Pause,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(Dimensions.xsPadding)
                                .size(mIcon),
                            tint = Color.White
                        )
                    }
                }
            }
        }

        AnimatedVisibility(timer.state == TimerState.PAUSED) {
            Box(
                Modifier
                    .offset(x = Dimensions.xsPadding)
                    .size(sIcon)
                    .background(MaterialTheme.colors.surface, CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        stopTimer()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Stop,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(Dimensions.xxsPadding)
                        .size(sIcon),
                    tint = Color.White
                )
            }
        }
    }
}