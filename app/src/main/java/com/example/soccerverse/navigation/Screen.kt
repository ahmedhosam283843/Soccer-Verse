package com.example.soccerverse.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Home : Screen("home_screen")
    object LeagueDetail: Screen("league_details_screen")
}