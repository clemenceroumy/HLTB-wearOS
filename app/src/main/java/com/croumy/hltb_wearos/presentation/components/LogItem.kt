package com.croumy.hltb_wearos.presentation.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.rotate
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
    isLoading: Boolean = false,
    onRefresh: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
        ), label = ""
    )

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

    LaunchedEffect(isLoading) {
        if(!isLoading) swipeState.snapTo("initial")
    }

    Box(
        Modifier.sizeIn(minHeight = Dimensions.lSize)
    ) {
        // BUTTONS REFRESH / CANCEL
        if(!log.saved) Row(
            Modifier
                .height(rowHeight.value)
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        secondary,
                        RoundedCornerShape(Dimensions.xlRadius, 0.dp, 0.dp, Dimensions.xlRadius)
                    )
                    .padding(end = Dimensions.mSize / 2)
                    .clip(RoundedCornerShape(Dimensions.xlRadius, 0.dp, 0.dp, Dimensions.xlRadius))
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
                    .background(
                        primary,
                        RoundedCornerShape(0.dp, Dimensions.xlRadius, Dimensions.xlRadius, 0.dp)
                    )
                    .padding(start = Dimensions.mSize / 2)
                    .clip(RoundedCornerShape(0.dp, Dimensions.xlRadius, Dimensions.xlRadius, 0.dp))
                    .clickable { onRefresh() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Refresh,
                    "retry sending session",
                    Modifier.rotate(if(isLoading) rotation.value else 0f)
                )
            }
        }

        // LOG CONTENT
        Row(
            Modifier
                .fillMaxSize()
                .offset(x = animatedOffset.value)
                .background(MaterialTheme.colors.surface, RoundedCornerShape(Dimensions.xlRadius))
                .border(1.dp, MaterialTheme.colors.surface, RoundedCornerShape(Dimensions.xlRadius))
                .clip(RoundedCornerShape(Dimensions.xlRadius))
                .onGloballyPositioned { coordinates ->
                    if (rowHeight.value == Dimensions.lSize) rowHeight.value =
                        with(density) { coordinates.size.height.toDp() }
                }
                .padding(horizontal = Dimensions.sPadding, vertical = Dimensions.xsPadding)
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
                    text = log.timePlayedTime.asString(
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