package ru.nn.tripnn.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.repository.AuthenticationService
import ru.nn.tripnn.domain.repository.TokenRepository
import ru.nn.tripnn.domain.util.RemoteResource
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    @Fake private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var message: String? by mutableStateOf(null)
        private set
    var isAuthenticated by mutableStateOf(false)
        private set
    
    var isEmailError by mutableStateOf(false)
        private set
    var isPasswordError by mutableStateOf(false)
        private set

    fun authenticate() {
        isLoading = true
        viewModelScope.launch {

            val token = tokenRepository.getToken()
            if (token == null) {
                isAuthenticated = false
                isLoading = false
                return@launch
            }

            when (val result = authenticationService.authenticate(token)) {
                is RemoteResource.Success -> {
                    isAuthenticated = true
                }

                is RemoteResource.Error -> {
                    message = result.message
                    isAuthenticated = false
                }
            }

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
data class ResourceState<T>(
    val value: T? = null,
    val isError: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false
)