package ru.nn.tripnn.ui.screen.authentication

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.repository.auth.AuthenticationService
import ru.nn.tripnn.data.repository.auth.TokenRepository
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.state.ResFlow
import ru.nn.tripnn.domain.state.ResState
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    @Fake private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _authenticated = ResFlow(
        initialState = ResState.Success(false),
        scope = viewModelScope,
        supplier = ::requestAuth
    )

    val isAuthenticated @Composable get() = _authenticated.state

    private fun requestAuth(): Flow<Result<Boolean>> {
        return tokenRepository.getToken().map { token ->
            val tokenValue = token.getOrThrow() ?: return@map Result.success(false)

            val auth = authenticationService.authenticate(tokenValue)
            if (auth.isSuccess) {
                return@map Result.success(true)
            }

            Result.failure(auth.exceptionOrNull() ?: Exception())
        }
    }

    fun logout() {
        viewModelScope.launch {
            val token = tokenRepository.getToken().first().getOrNull() ?: return@launch

            tokenRepository.deleteToken()
            authenticationService.logout(token)
        }
    }
}
