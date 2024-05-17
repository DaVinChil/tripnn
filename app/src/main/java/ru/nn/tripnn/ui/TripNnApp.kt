package ru.nn.tripnn.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.screen.authentication.AuthenticationViewModel
import ru.nn.tripnn.ui.screen.main.settings.UserSettingsViewModel
import ru.nn.tripnn.ui.screen.main.splash.HeartSplashScreen
import ru.nn.tripnn.ui.screen.navigation.AppRoutes
import ru.nn.tripnn.ui.screen.navigation.AuthRoutes
import ru.nn.tripnn.ui.screen.navigation.addAppGraph
import ru.nn.tripnn.ui.screen.navigation.addAuthGraph
import ru.nn.tripnn.ui.screen.navigation.rememberTripNnNavController
import ru.nn.tripnn.ui.theme.TripNNTheme

@Composable
fun TripNnApp() {
    val userSettingsViewModel = hiltViewModel<UserSettingsViewModel>()
    val authViewModel = hiltViewModel<AuthenticationViewModel>()

    val userSettings by userSettingsViewModel.userSettings.collectAsStateWithLifecycle()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()

    TripNNTheme(theme = userSettings.state?.theme!!) {
        var splashScreenFinished by rememberSaveable { mutableStateOf(false) }
        if (!splashScreenFinished) {
            HeartSplashScreen { splashScreenFinished = true }
        } else {
            TripNnAppNavigation(isAuthenticated)
        }
    }
}

@Composable
fun TripNnAppNavigation(isAuthenticated: ResourceState<Boolean>) {
    val tripNnNavController = rememberTripNnNavController()
    val startDestination = if (isAuthenticated.isSuccessAndNotNull()) {
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
