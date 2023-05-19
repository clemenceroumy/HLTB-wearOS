package com.croumy.hltb_wearos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.croumy.hltb_wearos.presentation.ui.game.GameDetails
import com.croumy.hltb_wearos.presentation.ui.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val actions = NavActions(navController = navController)

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                navigateToGame = {actions.navigateToGameDetails(it)}
            )
        }

        composable(
            NavRoutes.GameDetails.routeWithArgs,
            arguments = listOf(navArgument(NavRoutes.GameDetails.GAME_ID) { type = NavType.IntType })
        ) {
            GameDetails(
                onBack = {actions.navigateBack()},
            )
        }
    }
}
