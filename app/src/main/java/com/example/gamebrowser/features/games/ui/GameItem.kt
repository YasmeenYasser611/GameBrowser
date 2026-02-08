package com.example.gamebrowser.features.games.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gamebrowser.data.model.GameDto

@Composable
fun GameItem(game: GameDto) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = game.name,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = game.releaseDate ?: "Unknown release date",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
