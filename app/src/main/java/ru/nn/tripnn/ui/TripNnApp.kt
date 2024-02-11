package ru.nn.tripnn.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import ru.nn.tripnn.ui.navigation.AUTH_GRAPH_ROUTE
import ru.nn.tripnn.ui.navigation.TripNnNavController
import ru.nn.tripnn.ui.navigation.rememberTripNnNavController
import ru.nn.tripnn.ui.navigation.AppRoutes
import ru.nn.tripnn.ui.navigation.MAIN_GRAPH_ROUTE
import ru.nn.tripnn.ui.screen.MainViewModel
import ru.nn.tripnn.ui.navigation.addAppGraph
import ru.nn.tripnn.ui.navigation.addAuthGraph
import ru.nn.tripnn.ui.theme.TripNNTheme

@Composable
fun TripNnApp(viewModel: MainViewModel) {
    var curTheme by remember { mutableIntStateOf(2) }
    var curCurrency by remember { mutableIntStateOf(0) }
    var curLanguage by remember { mutableIntStateOf(0) }

    TripNNTheme(
        darkTheme = when (curTheme) {
            0 -> false
            1 -> true
            else -> isSystemInDarkTheme()
        }
    ) {
        val tripNnNavController = rememberTripNnNavController()

        NavHost(
            navController = tripNnNavController.navController,
            startDestination = AUTH_GRAPH_ROUTE
        ) {
            tripNnNavGraph(
                navController = tripNnNavController,
                viewModel = viewModel,
                curCurrency = curCurrency,
                curLanguage = curLanguage,
                curTheme = curTheme,
                onThemeChange = { curTheme = it },
                onCurrencyChange = { curCurrency = it },
                onLanguageChange = { curLanguage = it }
            )
        }
    }
}

private fun NavGraphBuilder.tripNnNavGraph(
    navController: TripNnNavController,
    onThemeChange: (Int) -> Unit,
    onCurrencyChange: (Int) -> Unit,
    onLanguageChange: (Int) -> Unit,
    viewModel: MainViewModel,
    curTheme: Int,
    curLanguage: Int,
    curCurrency: Int
) {
    addAuthGraph(
        navigateTo = navController::navigateTo,
        onSignUp = { navController.navigateTo(MAIN_GRAPH_ROUTE) },
        onLogIn = { navController.navigateTo(MAIN_GRAPH_ROUTE) },
        onForgot = {  }
    )

    addAppGraph(
        navigateTo = navController::navigateTo,
        onBack = navController::upPress,
        onThemeChange = onThemeChange,
        onLeaveAccount = {},
        viewModel = viewModel,
        onLanguageChange = onLanguageChange,
        onCurrencyChange = onCurrencyChange,
        currentTheme = curTheme,
        currentLanguage = curLanguage,
        currentCurrency = curCurrency
    )
}