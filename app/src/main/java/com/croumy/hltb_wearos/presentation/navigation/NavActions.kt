package com.croumy.hltb_wearos.presentation.navigation

import androidx.navigation.NavHostController
import com.croumy.hltb_wearos.presentation.models.api.GameInfo

class NavActions(private val navController: NavHostController) {
    fun navigateBack() {
        navController.popBackStack()
    }
    fun navigateToHome() {
        navController.popBackStack()
        navController.navigate(NavRoutes.Home.route)
    }

    fun navigateToGameDetails(id: Int) {
        navController.navigate(NavRoutes.GameDetails.routeWithArgs.replace("{${NavRoutes.GameDetails.ID}}", id.toString()))
    }

    fun navigateToAddGame(game: GameInfo) {
        navController.navigate(NavRoutes.AddGame.route)
        // SET GAME TO SAVEDSTATEHANDLE (RETRIEVE IN NavGraph.kt composable(NavRoutes.AddGame.route))
        navController.currentBackStackEntry?.savedStateHandle?.set("game", game)
    }
}