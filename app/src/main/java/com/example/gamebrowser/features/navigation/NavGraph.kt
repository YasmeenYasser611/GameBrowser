package com.example.gamebrowser.features.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gamebrowser.features.category.ui.CategoryScreen
import com.example.gamebrowser.features.gamedetails.ui.GameDetailsScreen
import com.example.gamebrowser.features.games.ui.GamesScreen
import com.example.gamebrowser.features.games.viewmodel.GamesViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.GamesList.route
    ) {
        composable(route = Screen.GamesList.route) {

            val sharedViewModel: GamesViewModel = hiltViewModel()

            GamesScreen(
                viewModel = sharedViewModel,
                onGameClick = { gameId ->
                    navController.navigate(Screen.GameDetails.createRoute(gameId))
                },
                onCategoryClick = { title, games ->

                    sharedViewModel.setCategoryData(title, games)

                    navController.navigate(Screen.Category.createRoute(title))
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

        composable(
            route = Screen.Category.route,
            arguments = listOf(
                navArgument("categoryTitle") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val parentEntry = navController.getBackStackEntry(Screen.GamesList.route)
            val sharedViewModel: GamesViewModel = hiltViewModel(parentEntry)

            CategoryScreen(
                categoryTitle = sharedViewModel.categoryTitle,
                games = sharedViewModel.categoryGames,
                onBackClick = {
                    navController.popBackStack()
                },
                onGameClick = { gameId ->
                    navController.navigate(Screen.GameDetails.createRoute(gameId))
                }
            )
        }
    }
}