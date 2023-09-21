package com.croumy.hltb_wearos.presentation.navigation

open class NavRoutes(val route: String) {
    object StartApp: NavRoutes("StartApp")
    object Home: NavRoutes("Home")
    object GameDetails: NavRoutes("GameDetails") {
        const val ID = "game_id"
        val routeWithArgs = "${this.route}/{$ID}"
    }
    object AddGame: NavRoutes("AddGame") {
        const val GAME = "game"
        val routeWithArgs = "${this.route}/{$GAME}"
    }
}