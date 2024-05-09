package ru.nn.tripnn.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import ru.nn.tripnn.data.local.usersettings.Theme

val LocalTripNnScheme = compositionLocalOf { LightColorScheme }

@Composable
fun getColorScheme(theme: Theme) = when (theme) {
    Theme.LIGHT -> LightColorScheme
    Theme.DARK -> DarkColorScheme
    Theme.MOONLIGHT -> MoonlightColorScheme
    Theme.SYSTEM -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
}

private val DarkColorScheme = TripNnColorScheme(
    background = Color(0xFF381831),
    secondaryBackground = Color(0xFF381831),
    primary = Color(0xFFFF8967),
    secondary = Color(0xFF4F2B47),
    onPrimary = Color(0xFFFFFFFF),
    cardBackground = Color(0xFF4F2B47),
    textColor = Color(0xFFF5F5F5),
    bottomSheetBackground = Color(0xFF381831),
    fieldBackground = Color(0xFF4F2B47),
    hint = Color(0xFFA57C9D),
    shadow = Color(0xFF1E2127),
    minor = Color(0xFF4F2B47),
    onMinor = Color(0xFFA57C9D),
    tertiary = Color(0xFFF5F5F5),
    currentRoute = Color(0xFF4F2B47),
    newRouteGlow = Color(0x55FFFFFF),
    undefined = Color(0xFF723E66)
)

private val MoonlightColorScheme = TripNnColorScheme(
    background = Color(0xFF000000),
    secondaryBackground = Color(0xFF000000),
    primary = Color(0xFFFF8967),
    secondary = Color(0xFF181818),
    onPrimary = Color(0xFFFFFFFF),
    cardBackground = Color(0xFF1F1F1F),
    textColor = Color(0xFFF1F1F1),
    bottomSheetBackground = Color(0xFF181818),
    fieldBackground = Color(0xFF2E2E2E),
    hint = Color(0xFFA9A9A9),
    shadow = Color(0xFF000000),
    minor = Color(0xFF2E2E2E),
    onMinor = Color(0xFFA9A9A9),
    tertiary = Color(0xFFF5F5F5),
    currentRoute = Color(0xFF1F1F1F),
    newRouteGlow = Color(0xFFFE8867),
    undefined = Color(0xFF3A3A3A)
)

private val LightColorScheme = TripNnColorScheme(
    background = Color(0xFFFFFFFF),
    secondaryBackground = Color(0xFFFFE577),
    primary = Color(0xFFFF8967),
    secondary = Color(0xFFFFE577),
    onPrimary = Color(0xFFFFFFFF),
    cardBackground = Color(0xFFFFFFFF),
    textColor = Color(0xFF392033),
    bottomSheetBackground = Color(0xFFFFFFFF),
    fieldBackground = Color(0xFFF6F6F6),
    hint = Color(0xFFBDBDBD),
    shadow = Color(0x40000000),
    minor = Color(0xFFF6F6F6),
    onMinor = Color(0xFFA9A9A9),
    tertiary = Color(0xFF392033),
    currentRoute = Color(0xFFFF8967),
    newRouteGlow = Color(0x55000000),
    undefined = Color(0xFFDDDDDD)
)

data class TripNnColorScheme(
    val background: Color,
    val secondaryBackground: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val cardBackground: Color,
    val textColor: Color,
    val bottomSheetBackground: Color,
    val fieldBackground: Color,
    val hint: Color,
    val shadow: Color,
    val minor: Color,
    val onMinor: Color,
    val tertiary: Color,
    val currentRoute: Color,
    val newRouteGlow: Color,
    val undefined: Color
)
