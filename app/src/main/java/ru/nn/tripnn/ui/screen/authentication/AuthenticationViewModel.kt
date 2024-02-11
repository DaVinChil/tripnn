package ru.nn.tripnn.ui.screen.authentication

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.entity.Credentials
import ru.nn.tripnn.domain.repository.AuthenticationService
import ru.nn.tripnn.domain.repository.TokenRepository
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val TOKEN = stringPreferencesKey("token")

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    @Fake private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var message: String? = null
        private set
    var isAuthenticated by mutableStateOf(false)
        private set

    init {
        authenticate()
    }

    private fun authenticate() {
        viewModelScope.launch {
            isLoading = true

            val token = tokenRepository.getToken()
            if (token == null) {
                message = "unauthenticated"
                isAuthenticated = false
                isLoading = false
                return@launch
            }

            when (val result = authenticationService.authenticate(token)) {
                is Resource.Success -> {
                    isAuthenticated = true
                }

                is Resource.Error -> {
                    message = result.message
                    isAuthenticated = false
                }
            }

            isLoading = false
        }
    }

    fun authenticate(credentials: Credentials) {
        viewModelScope.launch {
            isLoading = true

            when (val result = authenticationService.authenticate(credentials)) {
                is Resource.Success -> {
                    isAuthenticated = true
                    tokenRepository.saveToken(result.data!!)
                }

                is Resource.Error -> {
                    message = result.message
                    isAuthenticated = false
                }
            }

            isLoading = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            val token = tokenRepository.getToken() ?: return@launch

            tokenRepository.deleteToken()
            authenticationService.logout(token)
        }
    }
}
