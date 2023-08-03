package com.croumy.hltb_wearos.presentation.ui.home.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.croumy.hltb_wearos.R
import com.croumy.hltb_wearos.presentation.components.LogItem
import com.croumy.hltb_wearos.presentation.helpers.asString
import com.croumy.hltb_wearos.presentation.models.TimerState
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.soywiz.klock.Time
import com.soywiz.klock.TimeSpan
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LogsScreen(
    viewModel: LogsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = lifecycleOwner.lifecycleScope

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.getLogs()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(viewModel.appService.timer.value.state) {
        if (viewModel.appService.timer.value.state == TimerState.SAVED) {
            viewModel.appService.clearTimer()
            coroutineScope.launch { viewModel.getLogs() }
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            modifier,
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

            if (viewModel.logs.value.isNotEmpty()) ScalingLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimensions.xxsPadding),
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
                        LogItem(
                            log,
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
                }
            }
        }

        if(viewModel.logs.value.isEmpty()) Text(
            stringResource(id = R.string.no_logs),
            style = MaterialTheme.typography.body1.copy(color = Color.Gray),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}