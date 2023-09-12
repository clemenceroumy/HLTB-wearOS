package com.croumy.hltb_wearos.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltbwearos.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RefreshButton(
    refresh: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val animatedRotation = remember { Animatable(0f) }

    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.surface, CircleShape)
            .clickable {
                coroutineScope.launch {
                    this.launch {  animatedRotation.animateTo(
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 1000,
                                easing = LinearEasing
                            )
                        )
                    ) }
                    refresh()
                    delay(1000)
                    animatedRotation.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = 0,
                            easing = LinearEasing
                        )
                    )
                }
            }
            .padding(vertical = Dimensions.xsPadding, horizontal = Dimensions.sPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Rounded.Refresh,
            contentDescription = "refresh",
            Modifier
                .size(15.dp)
                .rotate(animatedRotation.value)
        )
        Spacer(Modifier.width(Dimensions.xxsPadding))
        Text(text = stringResource(id = R.string.refresh), style = MaterialTheme.typography.body2)
    }
}

@Preview
@Composable
fun RefreshButtonPreview() {
    RefreshButton {}
}