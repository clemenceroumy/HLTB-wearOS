package com.croumy.hltbwearos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.croumy.hltbwearos.ui.Home
import com.croumy.hltbwearos.ui.InfoScreen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "Home",
    ) {
        composable("Home") {
            Home(navigateToInfo = { navController.navigate("Information") })
        }

        composable("Information") {
            InfoScreen(navigateBack = { navController.popBackStack() })
        }
    }
}
