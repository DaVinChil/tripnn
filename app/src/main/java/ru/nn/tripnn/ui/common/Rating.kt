package ru.nn.tripnn.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun Rating(rating: Double, style: TextStyle = MaterialTheme.typography.headlineLarge) {
    MontsText(
        text = rating.toString(),
        color = getColorFromRating(rating),
        style = style
    )
}

fun getColorFromRating(rating: Double): Color {
    return if (rating >= 4) {
        Color(0xFF1DAB4D)
    } else if (rating >= 3) {
        Color(0xFFFFA462)
    } else {
        Color(0xFFFF6262)
    }
}