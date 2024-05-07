package ru.nn.tripnn.ui.screen.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.RegistrationData
import ru.nn.tripnn.data.remote.auth.AuthenticationService
import ru.nn.tripnn.data.local.token.TokenRepository
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.ui.screen.authentication.event.DismissAuthError
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    @Fake private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var authenticated by mutableStateOf(ResourceState(value = false))
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
                authenticated = authenticated.copy(
                    isError = true,
                    isLoading = false,
                    error = null,
                    value = false
                )

                return@launch
            }

            when (val result = authenticationService.register(
                RegistrationData(
                    name = userName,
                    password = password,
                    email = email
                )
            )
            ) {
                is RemoteResource.Success -> {
                    tokenRepository.saveToken(result.data!!)
                    authenticated = authenticated.copy(
                        isError = false,
                        isLoading = false,
                        error = null,
                        value = true
                    )
                    onSuccess()
                }

                is RemoteResource.Error -> {
                    authenticated = authenticated.copy(
                        isError = true,
                        isLoading = false,
                        error = result.message,
                        value = false
                    )
                }
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isBlank()) {
            emailState = emailState.copy(isError = true, error = "Email can not be empty")
            return false
        }

        emailState = emailState.copy(isError = false, error = null)

        return true
    }

    private fun validateUserName(userName: String): Boolean {
        if (userName.isBlank()) {
            userNameState =
                userNameState.copy(isError = true, error = "User name can not be empty")
            return false
        }

        userNameState =
            userNameState.copy(isError = false, error = null)

        return true
    }

    private fun validatePassword(password: String, confirmPassword: String): Boolean {
        if (password.isBlank() && confirmPassword.isBlank()) {
            passwordState =
                passwordState.copy(isError = true, error = "Password can not be empty")
            return false
        } else if (password != confirmPassword) {
            passwordState = passwordState.copy(isError = true, error = "Passwords do not match")
            return false
        }

        passwordState = passwordState.copy(isError = false, error = null)

        return true
    }

    fun dismissError(event: DismissAuthError) {
        viewModelScope.launch {
            when(event) {
                is DismissAuthError.EmailError -> dismissEmailError()
                is DismissAuthError.PasswordError -> dismissPasswordError()
                is DismissAuthError.UserNameError -> dismissUserNameError()
            }
        }
    }

    private fun dismissPasswordError() {
        passwordState = passwordState.copy(isError = false, error = null)
    }

    private fun dismissEmailError() {
        emailState = emailState.copy(isError = false, error = null)
    }

    private fun dismissUserNameError() {
        userNameState = userNameState.copy(isError = false, error = null)
    }
}