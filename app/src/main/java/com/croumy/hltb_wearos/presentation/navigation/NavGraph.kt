package com.croumy.hltb_wearos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.wear.compose.navigation.SwipeDismissableNavHost
//import androidx.wear.compose.navigation.composable
import androidx.navigation.compose.composable
import com.croumy.hltb_wearos.BuildConfig
import com.croumy.hltb_wearos.presentation.ui.game.GameDetails
import com.croumy.hltb_wearos.presentation.ui.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val actions = NavActions(navController = navController)

    NavHost(
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
            arguments = listOf(navArgument(NavRoutes.GameDetails.GAME_ID) { type = NavType.IntType }),
            deepLinks = listOf(navDeepLink {
                uriPattern = "app://${BuildConfig.APPLICATION_ID}/${NavRoutes.GameDetails.routeWithArgs}"
            }),
        ) {
            GameDetails(
                onBack = {actions.navigateBack()},
            )
        }
    }
}
