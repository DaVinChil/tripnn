package ru.nn.tripnn.ui.screen.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.repository.auth.AuthenticationService
import ru.nn.tripnn.data.repository.auth.TokenRepository
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.LoginData
import ru.nn.tripnn.domain.state.ResState
import ru.nn.tripnn.ui.screen.authentication.event.DismissAuthError
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Fake private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var authenticated: ResState<Boolean> by mutableStateOf(ResState.Success(false))
        private set
    var emailState: ResState<Unit> by mutableStateOf(ResState.Success(Unit))
        private set
    var passwordState: ResState<Unit> by mutableStateOf(ResState.Success(Unit))
        private set

    fun authenticate(
        rememberMe: Boolean,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            authenticated = ResState.loading()

            if (!validateEmail(email) or !validatePassword(password)) {
                authenticated = ResState.Error(
                    IllegalStateException("failed validate password or email")
                )
                return@launch
            }

            val result = authenticationService.login(LoginData(password = password, email = email))

            when {
                 result.isSuccess -> {
                    tokenRepository.saveToken(result.getOrNull()!!)
                    authenticated = ResState.Success(true)
                    onSuccess()
                }

                result.isFailure -> {
                    authenticated = ResState.Error(result.exceptionOrNull())
                }
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isBlank()) {
            emailState = ResState.Error(IllegalStateException("Email can not be empty"))
            return false
        }

        dismissEmailError()

        return true
    }

    private fun validatePassword(password: String): Boolean {
        if (password.isBlank()) {
            passwordState = ResState.Error(IllegalStateException("Password can not be empty"))
            return false
        }

        dismissPasswordError()

        return true
    }

    fun dismissError(event: DismissAuthError) {
        viewModelScope.launch {
            when (event) {
                is DismissAuthError.EmailError -> dismissEmailError()
                is DismissAuthError.PasswordError -> dismissPasswordError()
                is DismissAuthError.UserNameError -> {}
            }
        }
    }

    private fun dismissPasswordError() {
        passwordState = ResState.Success(Unit)
    }

    private fun dismissEmailError() {
        emailState = ResState.Success(Unit)
    }
}