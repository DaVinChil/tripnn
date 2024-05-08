package ru.nn.tripnn.ui.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import ru.nn.tripnn.ui.navigation.AppRoutes
import ru.nn.tripnn.ui.navigation.AuthRoutes
import ru.nn.tripnn.ui.navigation.addAppGraph
import ru.nn.tripnn.ui.navigation.addAuthGraph
import ru.nn.tripnn.ui.navigation.rememberTripNnNavController
import ru.nn.tripnn.ui.screen.authentication.AuthenticationViewModel
import ru.nn.tripnn.ui.screen.main.settings.UserSettingsViewModel
import ru.nn.tripnn.ui.screen.main.splash.HeartSplashScreen
import ru.nn.tripnn.ui.theme.TripNNTheme

@Composable
fun TripNnApp() {
    val userSettingsViewModel = hiltViewModel<UserSettingsViewModel>()
    val authViewModel = hiltViewModel<AuthenticationViewModel>()

    val userSettings = userSettingsViewModel.userSettingsState
    val isAuthenticated = authViewModel.isAuthenticated

    TripNNTheme(
        theme = userSettings.theme
    ) {
        var splashScreenFinished by rememberSaveable { mutableStateOf(false) }
        if (!splashScreenFinished) {
            HeartSplashScreen { splashScreenFinished = true }
        } else {
            TripNnAppNavigation(isAuthenticated)
        }
    }
}

@Composable
fun TripNnAppNavigation(isAuthenticated: Boolean) {
    val tripNnNavController = rememberTripNnNavController()
    val startDestination = if (isAuthenticated) {
        AppRoutes.MAIN_GRAPH_ROUTE
    } else {
        AuthRoutes.AUTH_GRAPH_ROUTE
    }

    NavHost(
        navController = tripNnNavController.navController,
        startDestination = startDestination
    ) {
        addAuthGraph(tripNnNavController)
        addAppGraph(tripNnNavController)
    }
}
