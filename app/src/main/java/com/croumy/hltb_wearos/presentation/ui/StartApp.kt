package com.croumy.hltb_wearos.presentation.ui

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Scaffold
import androidx.wear.remote.interactions.RemoteActivityHelper
import androidx.work.await
import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltb_wearos.presentation.components.LoginItem
import com.croumy.hltb_wearos.presentation.models.Constants
import com.croumy.hltb_wearos.presentation.models.Constants.Companion.PREFERENCES
import com.croumy.hltb_wearos.presentation.models.Constants.Companion.PREFERENCES_TOKEN
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.theme.green
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("RestrictedApi")
@Composable
fun StartApp(
    viewModel: StartAppViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val isLoggedIn = viewModel.appService.isLoggedIn.collectAsState()
    val isLoggingIn = viewModel.appService.isLoggingIn.collectAsState()

    LaunchedEffect(isLoggedIn.value) {
        // TRIGGERED WHEN MESSAGE RECEIVED FROM PHONE TO WATCH
        if(isLoggedIn.value) {
            if(isLoggedIn.value && isLoggingIn.value) {
                delay(400) // TIME TO SHOW THE CHECK ICON
            }
            navigateToHome()
        }
    }

    Scaffold {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isLoggedIn.value && !isLoggingIn.value) {
                LoginItem(
                    onLogin = {
                        viewModel.appService.isLoggingIn.value = true

                        // LAUNCH PHONE APP (WHICH WILL SEND BACK THE TOKEN AND USERID)
                        val intent = Intent(Intent.ACTION_VIEW)
                            .addCategory(Intent.CATEGORY_BROWSABLE)
                            .setData(Uri.parse(Constants.DEEPLINK_PHONE))

                        lifecycleOwner.lifecycleScope.launch {
                            try {
                                RemoteActivityHelper(context).startRemoteActivity(intent).await()
                            } catch (throwable: Throwable) {
                                //TODO: HANDLE CASE WHEN APP IS NOT INSTALLED ON THE HANDLED
                                Log.i("StartApp", "Error opening phone app : $throwable")
                            }
                        }
                    }
                )
            } else {
                AnimatedContent(
                    targetState = isLoggedIn.value && isLoggingIn.value, label = "",
                    modifier = Modifier.size(Dimensions.mSize),
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
                            Modifier.size(Dimensions.xsSize),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = Dimensions.xsStrokeSize,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(Dimensions.xxsPadding)
                            )
                        }
                }
            }
        }
    }
}