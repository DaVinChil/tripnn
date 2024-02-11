package ru.nn.tripnn.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import ru.nn.tripnn.ui.screen.application.TripNnApp
import ru.nn.tripnn.ui.screen.application.general.GeneralUiViewModel
import ru.nn.tripnn.ui.screen.application.splash.HeartSplashScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val generalUiViewModel by viewModels<GeneralUiViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var leaveSplash by remember {
                mutableStateOf(false)
            }

            if (leaveSplash) {
                TripNnApp(generalUiViewModel)
            } else {
                HeartSplashScreen({ leaveSplash = true }, generalUiViewModel.isLoading)
            }
        }
    }
}

