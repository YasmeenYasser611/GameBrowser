package com.example.gamebrowser.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Custom shapes for a modern gaming aesthetic
val Shapes = Shapes(
    // Small components like chips, small buttons, tags
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),

    // Medium components like cards, dialogs
    medium = RoundedCornerShape(12.dp),

    // Large components like bottom sheets, large cards
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)