package com.croumy.hltb_wearos.presentation.ui.startapp.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.green
import com.croumy.hltbwearos.R

@Composable
fun DoingLogin(
    isLoggedIn: Boolean,
    isLoggingIn: Boolean,
    cancel: () -> Unit
) {
    val currentView = LocalView.current

    DisposableEffect(Unit) {
        // KEEP SCREEN ON (APP DONT GO TO SLEEP MODE) WHILE WAITING TO RECEIVE MESSAGE FROM PHONE
        currentView.keepScreenOn = true
        onDispose { currentView.keepScreenOn = false }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = isLoggedIn && isLoggingIn, label = "",
            transitionSpec = {
                if (targetState) {
                    scaleIn(initialScale = 0.8f, animationSpec = tween(220, delayMillis = 90)) togetherWith scaleOut(animationSpec = tween(90))
                } else {
                    EnterTransition.None togetherWith scaleOut(animationSpec = tween(90))
                }
            }
        ) {
            if (it) Icon(
                Icons.Rounded.Check,
                contentDescription = "Done",
                tint = Color.White,
                modifier = Modifier
                    .size(Dimensions.mSize)
                    .background(green, CircleShape)
                    .padding(Dimensions.xsPadding)
            ) else
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = Dimensions.xsStrokeSize,
                        modifier = Modifier
                            .size(Dimensions.mSize)
                            .padding(Dimensions.xxsPadding)
                    )
                }
        }

        if(!isLoggedIn && isLoggingIn) {
            TextButton(
                onClick = { cancel() },
                Modifier
                    .align(Alignment.BottomCenter)
                    .clip(CircleShape)
            ) {
                Text(stringResource(id = R.string.cancel), style = MaterialTheme.typography.body2)
            }
        }
    }
}