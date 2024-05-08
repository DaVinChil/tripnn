package ru.nn.tripnn.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.valentinilk.shimmer.ShimmerTheme
import ru.nn.tripnn.data.local.usersettings.Theme

object TripNnTheme {
    val colorScheme: TripNnColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalTripNnScheme.current

    val shimmer: ShimmerTheme
        @Composable
        @ReadOnlyComposable
        get() = LocalShimmer.current
}

@Composable
fun TripNNTheme(
    theme: Theme = Theme.SYSTEM,
    content: @Composable () -> Unit
) {
    val colorScheme = getColorScheme(theme)
    val shimmer = getShimmer(theme = theme)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                theme != Theme.LIGHT
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    CompositionLocalProvider(
        LocalTripNnScheme provides colorScheme,
        LocalShimmer provides shimmer
    ) {
        MaterialTheme(
            typography = Typography,
            content = content
        )
    }
}