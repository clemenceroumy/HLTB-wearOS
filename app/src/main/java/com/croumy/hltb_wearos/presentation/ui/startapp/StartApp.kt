package com.croumy.hltb_wearos.presentation.ui.startapp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Scaffold
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.croumy.hltb_wearos.presentation.components.LoginItem
import com.croumy.hltb_wearos.presentation.helpers.Launcher
import com.croumy.hltb_wearos.presentation.models.Constants
import com.croumy.hltb_wearos.presentation.models.Constants.Companion.PHONE_CAPABILITY
import com.croumy.hltb_wearos.presentation.ui.startapp.components.DoingLogin
import com.croumy.hltb_wearos.presentation.ui.startapp.components.PhoneAppNotFound
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
    val hasPhoneApp = remember { mutableStateOf(true) }

    suspend fun checkConditions() {
        //CHECK IF PHONE HAS APP INSTALLED

        val capability = Wearable.getCapabilityClient(context).getCapability(
            PHONE_CAPABILITY,
            CapabilityClient.FILTER_REACHABLE
        ).await()

        hasPhoneApp.value = capability.nodes.isNotEmpty()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                lifecycleOwner.lifecycleScope.launch {
                    checkConditions()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(isLoggedIn.value) {
        // TRIGGERED WHEN MESSAGE RECEIVED FROM PHONE TO WATCH
        if (isLoggedIn.value) {
            if (isLoggedIn.value && isLoggingIn.value) {
                delay(400) // TIME TO SHOW THE CHECK ICON
            }
            viewModel.initCategories()
            navigateToHome()
        }
    }

    Scaffold {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!hasPhoneApp.value) {
                PhoneAppNotFound(refresh = { checkConditions() })
            } else if (!isLoggedIn.value && !isLoggingIn.value) {
                LoginItem(
                    onLogin = {
                        viewModel.appService.isLoggingIn.value = true

                        lifecycleOwner.lifecycleScope.launch {
                            Launcher.launchRemoteActivity(Constants.DEEPLINK_PHONE, context)
                        }
                    }
                )
            } else {
                DoingLogin(isLoggedIn.value, isLoggingIn.value, cancel = { viewModel.appService.isLoggingIn.value = false })
            }
        }
    }
}