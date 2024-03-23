package ru.nn.tripnn.ui.screen.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.nn.tripnn.ui.navigation.AUTH_GRAPH_ROUTE
import ru.nn.tripnn.ui.navigation.AppRoutes
import ru.nn.tripnn.ui.navigation.MAIN_GRAPH_ROUTE
import ru.nn.tripnn.ui.navigation.SPLASH_ROUTE
import ru.nn.tripnn.ui.navigation.TripNnNavController
import ru.nn.tripnn.ui.navigation.addAppGraph
import ru.nn.tripnn.ui.navigation.addAuthGraph
import ru.nn.tripnn.ui.navigation.rememberTripNnNavController
import ru.nn.tripnn.ui.screen.authentication.AuthenticationViewModel
import ru.nn.tripnn.ui.screen.main.settings.Theme
import ru.nn.tripnn.ui.screen.main.settings.UserSettingsViewModel
import ru.nn.tripnn.ui.screen.main.splash.HeartSplashScreen
import ru.nn.tripnn.ui.theme.TripNNTheme

@Composable
fun TripNnApp(
    userSettingsViewModel: UserSettingsViewModel,
    authViewModel: AuthenticationViewModel
) {
    val uiState = userSettingsViewModel.userSettingsState

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
                userSettingsViewModel = userSettingsViewModel,
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
                    isLoading = authViewModel.isLoading || userSettingsViewModel.isLoading
                )
            }
        }
    }
}

private fun NavGraphBuilder.tripNnNavGraph(
    navController: TripNnNavController,
    userSettingsViewModel: UserSettingsViewModel,
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
        userSettingsViewModel = userSettingsViewModel,
        onLeaveAccount = {
            navController.navigateTo(AUTH_GRAPH_ROUTE)
            authViewModel.logout()
        },
        navigateToPhotos = { placeId, initial ->
            navController.navController.navigate(AppRoutes.PHOTOS_ROUTE.route + "/$placeId/$initial") {
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}