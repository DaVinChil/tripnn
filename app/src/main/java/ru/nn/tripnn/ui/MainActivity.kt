package ru.nn.tripnn.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
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

        installSplashScreen()

        generalUiViewModel.loadPreferences()
        authViewModel.authenticate()

        setContent {
            TripNnApp(generalUiViewModel, authViewModel)
        }
    }
}
