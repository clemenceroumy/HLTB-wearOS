package com.croumy.hltb_wearos.presentation.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.croumy.hltb_wearos.presentation.models.api.GameInfo
import com.croumy.hltb_wearos.presentation.navigation.NavRoutes

class NavActions(private val navController: NavHostController) {
    fun navigateBack() {
        navController.popBackStack()
    }
    fun navigateToHome() {
        navController.popBackStack()
        navController.navigate(NavRoutes.Home.route)
    }

    fun navigateToGameDetails(gameId: Int) {
        navController.navigate(NavRoutes.GameDetails.routeWithArgs.replace("{${NavRoutes.GameDetails.GAME_ID}}", gameId.toString()))
    }

    fun navigateToAddGame(game: GameInfo) {
        navController.navigate(NavRoutes.AddGame.route)
        // SET GAME TO SAVEDSTATEHANDLE (RETRIEVE IN NavGraph.kt composable(NavRoutes.AddGame.route))
        navController.currentBackStackEntry?.savedStateHandle?.set("game", game)
    }
}