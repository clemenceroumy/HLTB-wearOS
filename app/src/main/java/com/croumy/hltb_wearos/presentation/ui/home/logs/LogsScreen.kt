package com.croumy.hltb_wearos.presentation.ui.home.logs

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.ui.components.LogItem
import com.croumy.hltbwearos.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LogsScreen(
    viewModel: LogsViewModel = hiltViewModel(),
    isFocusedScreen: Boolean = false,
    listState: ScalingLazyListState,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    refreshGames: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = lifecycleOwner.lifecycleScope

    val isClearing = remember { mutableStateOf(false) }

    LaunchedEffect(isFocusedScreen) {
        if (isFocusedScreen) {
            viewModel.getLogs()
            if (viewModel.logs.value.isNotEmpty()) {
                // WAIT FOR THE ScalingLazyColumn TO BE READY
                delay(300)
                focusRequester.requestFocus()
            }
        }
    }

    LaunchedEffect(viewModel.appService.timer.value.state) {
        if (viewModel.appService.timer.value.state == TimerState.SAVED) {
            viewModel.appService.clearTimer()
            refreshGames()
            coroutineScope.launch { viewModel.getLogs() }
        }
    }

    AnimatedContent(
        targetState = isClearing.value,
        label = "",
        modifier = modifier
    ) {
        if (it) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimensions.mPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(Modifier.height(0.dp))
                Text(
                    text = stringResource(id = R.string.logs_clear_all_confirm),
                    textAlign = TextAlign.Center
                )
                Row {
                    Box(modifier = Modifier
                        .clickable { isClearing.value = false }
                        .background(MaterialTheme.colors.surface, CircleShape)
                        .padding(Dimensions.xsPadding)
                    ) {
                        Icon(Icons.Default.Close, "cancel")
                    }
                    Spacer(Modifier.width(Dimensions.sPadding))
                    Box(modifier = Modifier
                        .clickable {
                            viewModel.deleteAllLogs()
                            isClearing.value = false
                        }
                        .background(MaterialTheme.colors.surface, CircleShape)
                        .padding(Dimensions.xsPadding)
                    ) {
                        Icon(Icons.Default.Check, "confirm clear")
                    }
                }
                Spacer(Modifier.height(0.dp))
            }
        } else {
            Box(
                Modifier.fillMaxSize()
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier
                            .padding(bottom = Dimensions.xxsPadding)
                            .background(Color.Gray, CircleShape)
                            .padding(
                                horizontal = Dimensions.sPadding,
                                vertical = Dimensions.xxsPadding
                            ),
                    ) { Text("Logs") }

                    if (viewModel.logs.value.isNotEmpty()) {
                        ScalingLazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = Dimensions.xxsPadding)
                                .onRotaryScrollEvent {
                                    coroutineScope.launch {
                                        listState.scrollBy(it.verticalScrollPixels)
                                    }
                                    true
                                }
                                .focusRequester(focusRequester)
                                .focusable(),
                        ) {
                            if (viewModel.failedLogs.isNotEmpty()) {
                                item {
                                    Text(
                                        stringResource(id = R.string.logs_history_errors),
                                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(bottom = Dimensions.xsPadding)
                                    )
                                }
                                items(viewModel.failedLogs) { log ->
                                    val isLoading =
                                        viewModel.appService.timer.value.state == TimerState.SAVING

                                    LogItem(
                                        log,
                                        isLoading = isLoading,
                                        onRefresh = { viewModel.resend(log) },
                                        onCancel = { viewModel.deleteLog(log) })
                                }
                                item {
                                    Spacer(Modifier.height(Dimensions.xsPadding))
                                }
                            }

                            if (viewModel.succeededLogs.isNotEmpty()) {
                                item {
                                    Text(
                                        stringResource(id = R.string.logs_history),
                                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(bottom = Dimensions.xsPadding)
                                    )
                                }
                                items(viewModel.succeededLogs) { log ->
                                    LogItem(log)
                                }
                                item {
                                    Spacer(Modifier.height(Dimensions.xsPadding))
                                    TextButton(onClick = { isClearing.value = true }) {
                                        Text(
                                            stringResource(id = R.string.logs_clear_all),
                                            style = MaterialTheme.typography.body1,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (viewModel.logs.value.isEmpty()) Text(
                    stringResource(id = R.string.no_logs),
                    style = MaterialTheme.typography.body1.copy(color = Color.Gray),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

}