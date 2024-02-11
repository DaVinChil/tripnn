package ru.nn.tripnn.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.nn.tripnn.ui.screen.application.TripNnApp
import ru.nn.tripnn.ui.screen.application.general.GeneralUiViewModel
import ru.nn.tripnn.ui.screen.authentication.AuthenticationViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val generalUiViewModel by viewModels<GeneralUiViewModel>()
    private val authViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TripNnApp(generalUiViewModel, authViewModel)
        }
    }
}

