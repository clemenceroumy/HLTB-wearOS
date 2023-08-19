package com.croumy.hltb_wearos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.SwipeDismissableNavHostState
import androidx.wear.compose.navigation.composable
import com.croumy.hltb_wearos.presentation.ui.StartApp
import com.croumy.hltb_wearos.presentation.ui.game.GameDetails
import com.croumy.hltb_wearos.presentation.ui.home.HomeScreen
import com.croumy.hltbwearos.BuildConfig

@Composable
fun NavGraph(navController: NavHostController, navState: SwipeDismissableNavHostState) {
    val actions = NavActions(navController = navController)

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = NavRoutes.StartApp.route,
        state = navState,
    ) {
        composable(NavRoutes.StartApp.route) {
            StartApp(
                navigateToHome = { actions.navigateToHome()}
            )
        }

        composable(NavRoutes.Home.route) {
            HomeScreen(
                navigateToGame = { actions.navigateToGameDetails(it)}
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
                onBack = { actions.navigateToHome() },
            )
        }
    }
}
