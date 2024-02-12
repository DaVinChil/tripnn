package ru.nn.tripnn.ui.screen.application

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.navigation.AUTH_GRAPH_ROUTE
import ru.nn.tripnn.ui.navigation.MAIN_GRAPH_ROUTE
import ru.nn.tripnn.ui.navigation.SPLASH_ROUTE
import ru.nn.tripnn.ui.navigation.TripNnNavController
import ru.nn.tripnn.ui.navigation.addAppGraph
import ru.nn.tripnn.ui.navigation.addAuthGraph
import ru.nn.tripnn.ui.navigation.rememberTripNnNavController
import ru.nn.tripnn.ui.screen.GeneralUiViewModel
import ru.nn.tripnn.ui.screen.Theme
import ru.nn.tripnn.ui.screen.application.splash.HeartSplashScreen
import ru.nn.tripnn.ui.screen.authentication.AuthenticationViewModel
import ru.nn.tripnn.ui.theme.TripNNTheme

@Composable
fun TripNnApp(
    generalUiViewModel: GeneralUiViewModel,
    authViewModel: AuthenticationViewModel,
    showSystemBars: (Boolean) -> Unit
) {
    val uiState = generalUiViewModel.uiPreferencesState

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
                generalUiViewModel = generalUiViewModel,
                authViewModel = authViewModel
            )

            composable(route = SPLASH_ROUTE) {
                LaunchedEffect(Unit) {
                    showSystemBars(false)
                }
                HeartSplashScreen(
                    onFinish = {
                        showSystemBars(true)
                        if (authViewModel.isAuthenticated) {
                            tripNnNavController.navigateTo(MAIN_GRAPH_ROUTE)
                        } else {
                            tripNnNavController.navigateTo(AUTH_GRAPH_ROUTE)
                        }
                    },
                    isLoading = authViewModel.isLoading
                )
            }
        }
    }
}

private fun NavGraphBuilder.tripNnNavGraph(
    navController: TripNnNavController,
    generalUiViewModel: GeneralUiViewModel,
    authViewModel: AuthenticationViewModel
) {
    addAuthGraph(
        navigateTo = navController::navigateTo,
        onSignUp = { credentials ->
            authViewModel.authenticate(credentials)
            navController.navigateTo(MAIN_GRAPH_ROUTE)
        },
        onLogIn = { rememberMe, credentials ->
            authViewModel.authenticate(credentials)
            navController.navigateTo(MAIN_GRAPH_ROUTE)
        },
        onForgot = { }
    )

    addAppGraph(
        navController = navController.navController,
        navigateTo = navController::navigateTo,
        onBack = navController::upPress,
        generalUiViewModel = generalUiViewModel,
        onLeaveAccount = {
            navController.navigateTo(AUTH_GRAPH_ROUTE)
            authViewModel.logout()
        }
    )
}