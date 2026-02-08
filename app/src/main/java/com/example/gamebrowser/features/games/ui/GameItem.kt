package com.example.gamebrowser.features.games.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gamebrowser.data.model.GameDto

@Composable
fun GameItem(game: GameDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(game.name, style = MaterialTheme.typography.titleMedium)
            Text(game.releaseDate ?: "Unknown", style = MaterialTheme.typography.bodySmall)
            Text("⭐ ${game.rating}")
        }
    }

}
