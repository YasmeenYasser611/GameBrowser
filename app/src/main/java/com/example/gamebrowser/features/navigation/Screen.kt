package com.example.gamebrowser.features.navigation

sealed class Screen(val route: String) {
    object GamesList : Screen("games_list")
    object GameDetails : Screen("game_details/{gameId}") {
        fun createRoute(gameId: Int) = "game_details/$gameId"
    }
}