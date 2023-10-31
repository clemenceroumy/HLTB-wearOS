package com.croumy.hltb_wearos.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.wear.compose.foundation.rememberSwipeToDismissBoxState
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.croumy.hltb_wearos.presentation.LocalNavController
import com.croumy.hltb_wearos.presentation.LocalNavSwipeBox
import com.croumy.hltb_wearos.presentation.theme.HLTBwearosTheme
import com.croumy.hltb_wearos.presentation.ui.game.GameDetailsScreen

@Composable
fun UISetup(body: @Composable () -> Unit) {
    val navController = rememberSwipeDismissableNavController()
    val swipeBoxState = rememberSwipeToDismissBoxState()

    HLTBwearosTheme {
        CompositionLocalProvider(
            LocalNavController provides navController,
            LocalNavSwipeBox provides swipeBoxState
        ) { body() }
    }
}