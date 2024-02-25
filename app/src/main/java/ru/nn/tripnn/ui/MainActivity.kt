package ru.nn.tripnn.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import ru.nn.tripnn.ui.screen.AuthenticationViewModel
import ru.nn.tripnn.ui.screen.UiPreferencesViewModel
import ru.nn.tripnn.ui.screen.main.TripNnApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val generalUiViewModel by viewModels<UiPreferencesViewModel>()
    private val authViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        generalUiViewModel.loadPreferences()
        authViewModel.authenticate()

        installSplashScreen()

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            TripNnApp(generalUiViewModel, authViewModel)
        }
    }
}

