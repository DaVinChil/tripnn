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
import ru.nn.tripnn.domain.state.ResState
import ru.nn.tripnn.ui.screen.authentication.event.DismissAuthError
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    @Fake private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    var authenticated: ResState<Boolean> by mutableStateOf(ResState.Success(false))
        private set
    var emailState: ResState<Unit> by mutableStateOf(ResState.Success(Unit))
        private set
    var userNameState: ResState<Unit> by mutableStateOf(ResState.Success(Unit))
        private set
    var passwordState: ResState<Unit> by mutableStateOf(ResState.Success(Unit))
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
            authenticated = ResState.loading()

            if (!validateEmail(email) or !validatePassword(password, confirmPassword)
                or !validateUserName(userName)
            ) {
                authenticated = ResState.Error()
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
                    authenticated = ResState.Success(true)
                    onSuccess()
                }

                result.isFailure -> authenticated = ResState.Error(result.exceptionOrNull())
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

    private fun validateUserName(userName: String): Boolean {
        if (userName.isBlank()) {
            userNameState = ResState.Error(IllegalStateException("User name can not be empty"))
            return false
        }

        dismissUserNameError()

        return true
    }

    private fun validatePassword(password: String, confirmPassword: String): Boolean {
        if (password.isBlank() && confirmPassword.isBlank()) {
            passwordState = ResState.Error(IllegalStateException("Password can not be empty"))
            return false
        } else if (password != confirmPassword) {
            passwordState = ResState.Error(IllegalStateException("Passwords do not match"))
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
        passwordState = ResState.Success(Unit)
    }

    private fun dismissEmailError() {
        emailState = ResState.Success(Unit)
    }

    private fun dismissUserNameError() {
        userNameState = ResState.Success(Unit)
    }
}