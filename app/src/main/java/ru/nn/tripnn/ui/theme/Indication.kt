package ru.nn.tripnn.ui.theme

import androidx.compose.foundation.Indication
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalIndication = compositionLocalOf<Indication> { error("no indication set") }

@Composable
fun rememberIndication(color: Color): Indication {
    return rememberRipple(color = color)
}