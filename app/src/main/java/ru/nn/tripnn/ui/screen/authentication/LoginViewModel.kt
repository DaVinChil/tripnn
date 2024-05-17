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
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.screen.authentication.event.DismissAuthError
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Fake private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var authenticated by mutableStateOf(ResourceState(state = false))
        private set
    var emailState by mutableStateOf(ResourceState<Unit>())
        private set
    var passwordState by mutableStateOf(ResourceState<Unit>())
        private set

    fun authenticate(
        rememberMe: Boolean,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            authenticated = ResourceState.loading()

            if (!validateEmail(email) or !validatePassword(password)) {
                authenticated = authenticated.toError(
                    IllegalStateException("failed validate password or email")
                )
                return@launch
            }

            val result = authenticationService.login(LoginData(password = password, email = email))

            when {
                 result.isSuccess -> {
                    tokenRepository.saveToken(result.getOrNull()!!)
                    authenticated = authenticated.toSuccess(true)
                    onSuccess()
                }

                result.isFailure -> {
                    authenticated = authenticated.toError(result.exceptionOrNull())
                }
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isBlank()) {
            emailState = emailState.toError(IllegalStateException("Email can not be empty"))
            return false
        }

        dismissEmailError()

        return true
    }

    private fun validatePassword(password: String): Boolean {
        if (password.isBlank()) {
            passwordState = passwordState.toError(IllegalStateException("Password can not be empty"))
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
        passwordState = passwordState.toSuccess()
    }

    private fun dismissEmailError() {
        emailState = emailState.toSuccess()
    }
}