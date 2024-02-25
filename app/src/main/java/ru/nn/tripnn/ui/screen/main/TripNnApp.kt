package ru.nn.tripnn.ui.screen.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.nn.tripnn.ui.navigation.AUTH_GRAPH_ROUTE
import ru.nn.tripnn.ui.navigation.MAIN_GRAPH_ROUTE
import ru.nn.tripnn.ui.navigation.SPLASH_ROUTE
import ru.nn.tripnn.ui.navigation.TripNnNavController
import ru.nn.tripnn.ui.navigation.addAppGraph
import ru.nn.tripnn.ui.navigation.addAuthGraph
import ru.nn.tripnn.ui.navigation.rememberTripNnNavController
import ru.nn.tripnn.ui.screen.AuthenticationViewModel
import ru.nn.tripnn.ui.screen.Theme
import ru.nn.tripnn.ui.screen.UiPreferencesViewModel
import ru.nn.tripnn.ui.screen.main.splash.HeartSplashScreen
import ru.nn.tripnn.ui.theme.TripNNTheme

@Composable
fun TripNnApp(
    uiPreferencesViewModel: UiPreferencesViewModel,
    authViewModel: AuthenticationViewModel
) {
    val uiState = uiPreferencesViewModel.uiPreferencesState

    TripNNTheme(
        darkTheme = when (uiState.theme) {
            Theme.LIGHT.id -> false
            Theme.DARK.id -> true
            else -> isSystemInDarkTheme()
        }
    ) {
        val tripNnNavController = rememberTripNnNavController()

        NavHost(
            navController = tripNnNavController.navController,
            startDestination = SPLASH_ROUTE
        ) {
            tripNnNavGraph(
                navController = tripNnNavController,
                uiPreferencesViewModel = uiPreferencesViewModel,
                authViewModel = authViewModel
            )

            composable(route = SPLASH_ROUTE) {
                HeartSplashScreen(
                    onFinish = {
                        if (authViewModel.isAuthenticated) {
                            tripNnNavController.navigateTo(MAIN_GRAPH_ROUTE)
                        } else {
                            tripNnNavController.navigateTo(AUTH_GRAPH_ROUTE)
                        }
                    },
                    isLoading = authViewModel.isLoading || uiPreferencesViewModel.isLoading
                )
            }
        }
    }
}

private fun NavGraphBuilder.tripNnNavGraph(
    navController: TripNnNavController,
    uiPreferencesViewModel: UiPreferencesViewModel,
    authViewModel: AuthenticationViewModel
) {
    addAuthGraph(
        navigateTo = navController::navigateTo,
        authViewModel = authViewModel,
        navController = navController
    )

    addAppGraph(
        navController = navController.navController,
        navigateTo = navController::navigateTo,
        onBack = navController::upPress,
        uiPreferencesViewModel = uiPreferencesViewModel,
        onLeaveAccount = {
            navController.navigateTo(AUTH_GRAPH_ROUTE)
            authViewModel.logout()
        }
    )
}