package com.croumy.hltb_wearos.presentation.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.croumy.hltb_wearos.presentation.data.database.entity.LogEntity
import com.croumy.hltb_wearos.presentation.helpers.asString
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.primary
import com.croumy.hltb_wearos.presentation.theme.secondary
import com.soywiz.klock.Time
import com.soywiz.klock.TimeSpan
import kotlinx.coroutines.launch

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun LogItem(
    log: LogEntity,
    onRefresh: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    val swipeState = rememberSwipeableState(initialValue = "initial")
    val anchors = mapOf(0f to "left", 0.5f to "initial", 1f to "right")
    val previousState = remember { mutableStateOf("initial") }

    val density = LocalDensity.current
    val rowHeight = remember { mutableStateOf(Dimensions.lSize) }

    val animatedOffset = animateDpAsState(
        targetValue = when (swipeState.currentValue) {
            "left" -> -Dimensions.mSize
            "right" -> Dimensions.mSize
            else -> 0.dp
        }, label = ""
    )

    LaunchedEffect(swipeState.currentValue) {
        if (previousState.value != "initial") { swipeState.snapTo("initial") }
        previousState.value = swipeState.currentValue
    }

    Box(
        Modifier.sizeIn(minHeight = Dimensions.lSize)
    ) {
        // BUTTONS REFRESH / CANCEL
        if(!log.saved) Row(
            Modifier
                .height(rowHeight.value)
                .fillMaxHeight()
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(secondary, RoundedCornerShape(100, 0, 0, 100))
                    .padding(end = Dimensions.mSize / 2)
                    .clip(RoundedCornerShape(100, 0, 0, 100))
                    .clickable {
                        onCancel()
                        coroutineScope.launch { swipeState.snapTo("initial") }
                    },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.Close, "cancel session") }

            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(primary, RoundedCornerShape(0, 100, 100, 0))
                    .padding(start = Dimensions.mSize / 2)
                    .clip(RoundedCornerShape(0, 100, 100, 0))
                    .clickable {
                        onRefresh()
                        coroutineScope.launch { swipeState.snapTo("initial") }
                    },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.Refresh, "retry sending session") }
        }

        // LOG CONTENT
        Row(
            Modifier
                .fillMaxSize()
                .offset(x = animatedOffset.value)
                .background(MaterialTheme.colors.surface, RoundedCornerShape(100))
                .clip(RoundedCornerShape(100))
                .onGloballyPositioned { coordinates ->
                    if (rowHeight.value == Dimensions.lSize) rowHeight.value =
                        with(density) { coordinates.size.height.toDp() }
                }
                .padding(horizontal = Dimensions.mPadding, vertical = Dimensions.sPadding)
                .swipeable(
                    state = swipeState,
                    anchors = anchors,
                    enabled = !log.saved,
                    orientation = Orientation.Horizontal,
                    interactionSource = null,
                ),
        ) {
            Column(
                Modifier.weight(1f)
            ) {
                Text(
                    text = log.title,
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(Dimensions.xxsPadding))
                Text(
                    text = Time(TimeSpan(log.timePlayed.toDouble())).asString(
                        withSeconds = true,
                        withZeros = true,
                        withStringUnit = true
                    )
                )
                Text(text = log.date.asString(), style = MaterialTheme.typography.body2)
            }
        }
    }
}