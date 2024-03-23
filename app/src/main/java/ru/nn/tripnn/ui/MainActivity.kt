package ru.nn.tripnn.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import ru.nn.tripnn.ui.screen.authentication.AuthenticationViewModel
import ru.nn.tripnn.ui.screen.main.settings.UserSettingsViewModel
import ru.nn.tripnn.ui.screen.main.TripNnApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userSettingsViewModel by viewModels<UserSettingsViewModel>()
    private val authViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        installSplashScreen()

        userSettingsViewModel.loadPreferences()
        authViewModel.authenticate()

        setContent {
            TripNnApp(userSettingsViewModel, authViewModel)
        }
    }
}