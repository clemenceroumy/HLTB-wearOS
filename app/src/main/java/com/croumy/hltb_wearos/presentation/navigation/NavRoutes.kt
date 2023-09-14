package com.croumy.hltb_wearos.presentation.navigation

open class NavRoutes(val route: String) {
    object StartApp: NavRoutes("StartApp")
    object Home: NavRoutes("Home")
    object GameDetails: NavRoutes("GameDetails") {
        const val GAME_ID = "game_id"
        val routeWithArgs = "${this.route}/{$GAME_ID}"
    }
    object AddGame: NavRoutes("AddGame")
}