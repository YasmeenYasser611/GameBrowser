package com.example.gamebrowser

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.gamebrowser.features.games.ui.GamesScreen

@Composable
fun GameBrowserRoot() {
    Surface {
        GamesScreen()
    }
}
