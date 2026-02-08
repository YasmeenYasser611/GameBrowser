package com.example.gamebrowser

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.gamebrowser.features.navigation.NavGraph

@Composable
fun GameBrowserRoot() {
    val navController = rememberNavController()

    Surface {
        NavGraph(navController = navController)
    }
}