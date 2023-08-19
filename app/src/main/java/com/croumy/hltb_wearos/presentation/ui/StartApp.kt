package com.croumy.hltb_wearos.presentation.ui

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.wear.remote.interactions.RemoteActivityHelper
import androidx.work.await
import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltb_wearos.presentation.models.Constants
import com.croumy.hltb_wearos.presentation.models.Constants.Companion.PREFERENCES
import com.croumy.hltb_wearos.presentation.models.Constants.Companion.PREFERENCES_TOKEN
import com.croumy.hltb_wearos.presentation.theme.Dimensions
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@SuppressLint("RestrictedApi")
@Composable
fun StartApp(
    viewModel: StartAppViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_START) {
                // CHECK IF USER IS LOGGED IN (USERID and TOKEN EXISTS IN LOCAL STORAGE)
                val token = viewModel.preferencesService.token

                if(!token.isNullOrEmpty()) {
                    navigateToHome()
                } else {
                    // LAUNCH PHONE APP
                    val intent = Intent(Intent.ACTION_VIEW)
                        .addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse(Constants.DEEPLINK_PHONE))

                    lifecycleOwner.lifecycleScope.launch {
                        try {
                            RemoteActivityHelper(context).startRemoteActivity(intent).await()
                        } catch (cancellationException: CancellationException) {
                            throw cancellationException
                        } catch (throwable: Throwable) {
                            //TODO: HANDLE CASE WHEN APP IS NOT INSTALLED ON THE HANDLED
                        }
                    }
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = Dimensions.xsStrokeSize,
            modifier = Modifier.size(Dimensions.mSize)
        )
    }
}