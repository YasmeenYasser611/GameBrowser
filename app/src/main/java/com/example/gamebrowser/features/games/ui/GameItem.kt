package com.example.gamebrowser.features.games.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameItem(
    title: String,
    rating: Double,
    releaseDate: String?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Rating: $rating",
                style = MaterialTheme.typography.bodyMedium
            )

            releaseDate?.let {
                Text(
                    text = "Release: $it",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
