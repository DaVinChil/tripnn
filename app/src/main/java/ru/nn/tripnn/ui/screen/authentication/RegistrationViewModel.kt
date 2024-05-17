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
import ru.nn.tripnn.domain.RegistrationData
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.screen.authentication.event.DismissAuthError
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    @Fake private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var authenticated by mutableStateOf(ResourceState(state = false))
        private set
    var emailState by mutableStateOf(ResourceState<Unit>())
        private set
    var userNameState by mutableStateOf(ResourceState<Unit>())
        private set
    var passwordState by mutableStateOf(ResourceState<Unit>())
        private set

    fun authenticate(
        rememberMe: Boolean,
        email: String,
        userName: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            authenticated = authenticated.copy(isLoading = true)

            if (!validateEmail(email) or !validatePassword(password, confirmPassword)
                or !validateUserName(userName)
            ) {
                authenticated = authenticated.toError()
                return@launch
            }

            val result = authenticationService.register(
                RegistrationData(
                    name = userName,
                    password = password,
                    email = email
                )
            )

            when {
                result.isSuccess -> {
                    tokenRepository.saveToken(result.getOrNull()!!)
                    authenticated = authenticated.toSuccess(true)
                    onSuccess()
                }

                result.isFailure -> authenticated = authenticated.toError(result.exceptionOrNull())
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isBlank()) {
            emailState = emailState.toError(IllegalStateException("Email can not be empty"))
            return false
        }

        emailState = emailState.copy(isError = false, error = null)

        return true
    }

    private fun validateUserName(userName: String): Boolean {
        if (userName.isBlank()) {
            userNameState = userNameState.toError(IllegalStateException("User name can not be empty"))
            return false
        }

        dismissUserNameError()

        return true
    }

    private fun validatePassword(password: String, confirmPassword: String): Boolean {
        if (password.isBlank() && confirmPassword.isBlank()) {
            passwordState = passwordState.toError(IllegalStateException("Password can not be empty"))
            return false
        } else if (password != confirmPassword) {
            passwordState = passwordState.toError(IllegalStateException("Passwords do not match"))
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
                is DismissAuthError.UserNameError -> dismissUserNameError()
            }
        }
    }

    private fun dismissPasswordError() {
        passwordState = passwordState.toSuccess()
    }

    private fun dismissEmailError() {
        emailState = emailState.toSuccess()
    }

    private fun dismissUserNameError() {
        userNameState = userNameState.toSuccess()
    }
}