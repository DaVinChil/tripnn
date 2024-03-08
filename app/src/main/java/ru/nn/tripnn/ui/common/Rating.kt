package ru.nn.tripnn.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun Rating(rating: Double, fontSize: TextUnit = 12.sp) {
    MontsText(
        text = rating.toString(),
        fontSize = fontSize,
        color = getColorFromRating(rating),
        fontWeight = FontWeight.Bold
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