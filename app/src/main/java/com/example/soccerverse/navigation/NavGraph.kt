package com.example.soccerverse.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.soccerverse.SplashScreen
import com.example.soccerverse.leaguedetailscreen.LeagueDetailScreen
import com.example.soccerverse.leaguelistscreen.LeagueListScreen
import com.example.soccerverse.teamdetailscreen.TeamDetailScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            LeagueListScreen(navController = navController)
        }
        composable(
            "${Screen.LeagueDetail.route}/{dominantColor}/{leagueId}",
            arguments = listOf(
                navArgument("dominantColor") {
                    type = NavType.IntType
                },
                navArgument("leagueId") {
                    type = NavType.IntType
                }
            )
        ) {
            val dominantColor = remember {
                val color = it.arguments?.getInt("dominantColor")
                color?.let { Color(it) } ?: Color.White
            }
            val leagueId = remember {
                it.arguments!!.getInt("leagueId")
            }
            LeagueDetailScreen(
                dominantColor = dominantColor,
                leagueId = leagueId,
                navController = navController
            )

        }
        composable(
            "${Screen.TeamDetail.route}/{teamId}/{leagueId}/{dominantColor}",
            arguments = listOf(
                navArgument("teamId") {
                    type = NavType.IntType
                },
                navArgument("leagueId") {
                    type = androidx.navigation.NavType.IntType
                },
                navArgument("dominantColor") {
                    type = NavType.IntType
                },

            )
        ) {
            val teamId = remember {
                it.arguments!!.getInt("teamId")
            }

            val leagueId = remember {
                it.arguments!!.getInt("leagueId")
            }
            val dominantColor = remember {
                val color = it.arguments?.getInt("dominantColor")
                color?.let { Color(it) } ?: Color.White
            }
            TeamDetailScreen(
                teamId = teamId,
                leagueId = leagueId,
                dominantColor = dominantColor,
                navController = navController
            )

        }
    }
}