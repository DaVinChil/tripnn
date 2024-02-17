package ru.nn.tripnn.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import ru.nn.tripnn.ui.screen.application.TripNnApp
import ru.nn.tripnn.ui.screen.GeneralUiViewModel
import ru.nn.tripnn.ui.screen.authentication.AuthenticationViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val generalUiViewModel by viewModels<GeneralUiViewModel>()
    private val authViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

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

