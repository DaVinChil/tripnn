package ru.nn.tripnn.ui.screen.application

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.nn.tripnn.ui.navigation.AUTH_GRAPH_ROUTE
import ru.nn.tripnn.ui.navigation.MAIN_GRAPH_ROUTE
import ru.nn.tripnn.ui.navigation.TripNnNavController
import ru.nn.tripnn.ui.navigation.addAppGraph
import ru.nn.tripnn.ui.navigation.addAuthGraph
import ru.nn.tripnn.ui.navigation.rememberTripNnNavController
import ru.nn.tripnn.ui.screen.application.general.GeneralUiViewModel
import ru.nn.tripnn.ui.theme.TripNNTheme

@Composable
fun TripNnApp(
    generalUiViewModel: GeneralUiViewModel
) {
    val uiState = generalUiViewModel.uiPreferencesState

    TripNNTheme(
        darkTheme = when (uiState.theme) {
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
                curCurrency = uiState.currency,
                curLanguage = uiState.language,
                curTheme = uiState.theme,
                onThemeChange = generalUiViewModel::changeTheme,
                onCurrencyChange = generalUiViewModel::changeCurrency,
                onLanguageChange = generalUiViewModel::changeLanguage
            )
        }
    }
}

private fun NavGraphBuilder.tripNnNavGraph(
    navController: TripNnNavController,
    onThemeChange: (Int) -> Unit,
    onCurrencyChange: (Int) -> Unit,
    onLanguageChange: (Int) -> Unit,
    curTheme: Int,
    curLanguage: Int,
    curCurrency: Int
) {
    addAuthGraph(
        navigateTo = navController::navigateTo,
        onSignUp = { navController.navigateTo(MAIN_GRAPH_ROUTE) },
        onLogIn = { navController.navigateTo(MAIN_GRAPH_ROUTE) },
        onForgot = { }
    )

    addAppGraph(
        navController = navController.navController,
        navigateTo = navController::navigateTo,
        onBack = navController::upPress,
        onThemeChange = onThemeChange,
        onLeaveAccount = {},
        onLanguageChange = onLanguageChange,
        onCurrencyChange = onCurrencyChange,
        currentTheme = curTheme,
        currentLanguage = curLanguage,
        currentCurrency = curCurrency
    )
}