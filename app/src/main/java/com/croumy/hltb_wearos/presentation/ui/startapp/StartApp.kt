package com.croumy.hltb_wearos.presentation.ui.startapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.croumy.hltb_wearos.presentation.helpers.Launcher
import com.croumy.hltb_wearos.presentation.models.Constants
import com.croumy.hltb_wearos.presentation.models.Constants.Companion.PHONE_CAPABILITY
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import com.croumy.hltb_wearos.presentation.ui.components.LoginItem
import com.croumy.hltb_wearos.presentation.ui.startapp.components.DoingLogin
import com.croumy.hltb_wearos.presentation.ui.startapp.components.PhoneAppNotFound
import com.croumy.hltbwearos.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPermissionsApi::class)
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
    val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS) else null
    val notificationDenied = remember { mutableStateOf(false) }

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

    LaunchedEffect(isLoggedIn.value, notificationPermission?.status) {
        // TRIGGERED WHEN MESSAGE RECEIVED FROM PHONE TO WATCH
        if (isLoggedIn.value) {
            if (isLoggingIn.value) {
                delay(400)
            } // TIME TO SHOW THE CHECK ICON
            if (notificationPermission == null || notificationPermission.status == PermissionStatus.Granted) {
                viewModel.initWithUserData()
                navigateToHome()
            } else if (notificationPermission.status == PermissionStatus.Denied(true)) {
                if(isLoggingIn.value) {
                    notificationDenied.value = true
                    delay(2000)
                }
                navigateToHome()
            } else {
                notificationPermission.launchPermissionRequest()
            }
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
                if (notificationDenied.value) {
                    Text(
                        text = stringResource(id = R.string.notifications_denied),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(Dimensions.sPadding)
                    )
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = Dimensions.xsStrokeSize,
                        modifier = Modifier.size(Dimensions.sSize)
                    )
                } else DoingLogin(isLoggedIn.value, isLoggingIn.value, cancel = { viewModel.appService.isLoggingIn.value = false })
            }
        }
    }
}