package ru.nn.tripnn.ui.screen.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.data.remote.auth.AuthenticationService
import ru.nn.tripnn.data.local.token.TokenRepository
import ru.nn.tripnn.data.RemoteResource
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    @Fake private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var isAuthenticated by mutableStateOf(false)
        private set

    init {
        authenticate()
    }

    fun authenticate() {
        isLoading = true
        viewModelScope.launch {
            val token = tokenRepository.getToken()
            if (token == null) {
                isAuthenticated = false
                isLoading = false
                return@launch
            }

            isAuthenticated = authenticationService.authenticate(token) is RemoteResource.Success
            isLoading = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            isAuthenticated = false

            val token = tokenRepository.getToken() ?: return@launch

            tokenRepository.deleteToken()
            authenticationService.logout(token)
        }
    }
}
