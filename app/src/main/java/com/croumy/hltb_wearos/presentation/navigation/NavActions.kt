package com.croumy.hltb_wearos.presentation.navigation

import androidx.navigation.NavHostController
import com.croumy.hltb_wearos.presentation.navigation.NavRoutes

class NavActions(private val navController: NavHostController) {
    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToGameDetails(gameId: Int) {
        navController.navigate(NavRoutes.GameDetails.routeWithArgs.replace("{${NavRoutes.GameDetails.GAME_ID}}", gameId.toString()))
    }
}