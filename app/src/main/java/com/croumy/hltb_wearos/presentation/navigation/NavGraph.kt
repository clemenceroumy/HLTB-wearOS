package com.croumy.hltb_wearos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.croumy.hltb_wearos.presentation.ui.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val actions = NavActions(navController = navController)

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen()
        }
    }
}
