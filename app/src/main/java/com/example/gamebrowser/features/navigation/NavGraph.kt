package com.example.gamebrowser.features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gamebrowser.features.gamedetails.ui.GameDetailsScreen
import com.example.gamebrowser.features.games.ui.GamesScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.GamesList.route
    ) {
        composable(route = Screen.GamesList.route) {
            GamesScreen(
                onGameClick = { gameId ->
                    navController.navigate(Screen.GameDetails.createRoute(gameId))
                }
            )
        }

        composable(
            route = Screen.GameDetails.route,
            arguments = listOf(
                navArgument("gameId") {
                    type = NavType.StringType
                }
            )
        ) {
            GameDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}