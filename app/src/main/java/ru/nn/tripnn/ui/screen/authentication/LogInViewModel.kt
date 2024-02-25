package ru.nn.tripnn.ui.screen.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.entity.LogInData
import ru.nn.tripnn.domain.repository.AuthenticationService
import ru.nn.tripnn.domain.repository.TokenRepository
import ru.nn.tripnn.domain.util.Resource
import ru.nn.tripnn.ui.event.Dismiss
import ru.nn.tripnn.ui.screen.RemoteResource
import ru.nn.tripnn.ui.screen.ResourceState
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    @Fake private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var authenticated by mutableStateOf(RemoteResource(value = false))
        private set
    var emailState by mutableStateOf(ResourceState<Unit>())
        private set
    var passwordState by mutableStateOf(ResourceState<Unit>())
        private set

    fun authenticate(
        rememberMe: Boolean,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            authenticated = authenticated.copy(isLoading = true)

            if (!validateEmail(email) or !validatePassword(password)) {
                authenticated = authenticated.copy(
                    isError = true,
                    isLoading = false,
                    message = null,
                    value = false
                )

                return@launch
            }

            when (val result = authenticationService.login(
                LogInData(
                    password = password,
                    email = email
                )
            )
            ) {
                is Resource.Success -> {
                    tokenRepository.saveToken(result.data!!)
                    authenticated = authenticated.copy(
                        isError = false,
                        isLoading = false,
                        message = null,
                        value = true
                    )
                }

                is Resource.Error -> {
                    authenticated = authenticated.copy(
                        isError = true,
                        isLoading = false,
                        message = result.message,
                        value = false
                    )
                }
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isBlank()) {
            emailState = emailState.copy(isError = true, message = "Email can not be empty")
            return false
        }

        emailState = emailState.copy(isError = false, message = null)

        return true
    }

    private fun validatePassword(password: String): Boolean {
        if (password.isBlank()) {
            passwordState = passwordState.copy(isError = true, message = "Password can not be empty")
            return false
        }

        passwordState = passwordState.copy(isError = false, message = null)

        return true
    }

    fun dismissError(event: Dismiss) {
        viewModelScope.launch {
            when(event) {
                is Dismiss.EmailError -> dismissEmailError()
                is Dismiss.PasswordError -> dismissPasswordError()
                is Dismiss.UserNameError -> {}
            }
        }
    }

    private fun dismissPasswordError() {
        passwordState = passwordState.copy(isError = false, message = null)
    }

    private fun dismissEmailError() {
        emailState = emailState.copy(isError = false, message = null)
    }
}