package ru.nn.tripnn.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import com.valentinilk.shimmer.defaultShimmerTheme
import ru.nn.tripnn.data.database.usersettings.Theme

val LocalShimmer = compositionLocalOf { lightShimmer }

@Composable
fun getShimmer(theme: Theme) = when(theme) {
    Theme.LIGHT -> darkShimmer
    else -> lightShimmer
}

val lightShimmer = defaultShimmerTheme.copy(
    animationSpec = infiniteRepeatable(
        animation = tween(
            durationMillis = 1000,
            delayMillis = 1_200,
            easing = LinearEasing,
        ),
    ),
    blendMode = BlendMode.Hardlight,
    rotation = 25f,
    shaderColors = listOf(
        Color.White.copy(alpha = 0.0f),
        Color.White.copy(alpha = 0.2f),
        Color.White.copy(alpha = 0.0f),
    ),
    shaderColorStops = null
)

val darkShimmer = defaultShimmerTheme.copy(
    animationSpec = infiniteRepeatable(
        animation = tween(
            durationMillis = 1000,
            delayMillis = 1_200,
            easing = LinearEasing,
        ),
    ),
    blendMode = BlendMode.Hardlight,
    rotation = 25f,
    shaderColors = listOf(
        Color.Black.copy(alpha = 0.0f),
        Color.Black.copy(alpha = 0.2f),
        Color.Black.copy(alpha = 0.0f),
    ),
    shaderColorStops = null
)
