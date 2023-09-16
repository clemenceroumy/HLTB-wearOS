package com.croumy.hltb_wearos.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.wear.compose.material.SwipeToDismissBoxState
import androidx.wear.compose.material.rememberSwipeToDismissBoxState
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.navigation.rememberSwipeDismissableNavHostState
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.data.PreferencesService
import com.croumy.hltb_wearos.presentation.models.Constants
import com.croumy.hltb_wearos.presentation.models.Constants.Companion.DATA_LAYER_TOKEN_CHANNEL
import com.croumy.hltb_wearos.presentation.navigation.NavGraph
import com.croumy.hltb_wearos.presentation.theme.HLTBwearosTheme
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

val LocalNavController = staticCompositionLocalOf<NavHostController> { error("No SwipeDismissableNavHostState found!") }
val LocalNavSwipeBox = staticCompositionLocalOf<SwipeToDismissBoxState> { error("No SwipeToDismissBoxState found!") }

@AndroidEntryPoint
class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener {

    // CREATING AN INSTANCE OF THE SERVICE WHEN APP STARTS
    @Inject
    lateinit var appService: AppService

    @Inject
    lateinit var hltbService: HLTBService

    @Inject
    lateinit var preferencesService: PreferencesService

    companion object {
        lateinit var context: Context
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext

        Wearable.getMessageClient(this).addListener(this)

        setContent {
            val navController = rememberSwipeDismissableNavController()
            val swipeBoxState = rememberSwipeToDismissBoxState()
            val navState = rememberSwipeDismissableNavHostState(swipeBoxState)

            HLTBwearosTheme {
                CompositionLocalProvider(
                    LocalNavController provides navController,
                    LocalNavSwipeBox provides swipeBoxState
                ) { NavGraph(navController = navController, navState = navState) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Wearable.getMessageClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()
        Wearable.getMessageClient(this).removeListener(this)
    }

    override fun onMessageReceived(message: MessageEvent) {
        Log.i("MainActivity", "Message received: ${message.path}")

        if (message.path == DATA_LAYER_TOKEN_CHANNEL) {
            lifecycleScope.launch {
                // RETRIEVE TOKEN FROM PHONE AND STORE IT IN WATCH
                val token = String(message.data)
                preferencesService.token = token

                // RETRIEVE USERID WITH TOKEN AND STORE IT
                val user = hltbService.getUser()
                preferencesService.userId = user?.user_id?.toInt()

                // SETTING THE VALUE OF THE FLOW (LISTEN INSIDE StartApp.kt TO REDIRECT TO HOME)
                appService.isLoggedIn.value = true

                val phoneNodeId = message.sourceNodeId
                Wearable.getMessageClient(this@MainActivity).sendMessage(
                    phoneNodeId,
                    Constants.DATA_LAYER_DATA_RECEIVED,
                    "true".toByteArray()
                ).await()
            }
        }
    }
}