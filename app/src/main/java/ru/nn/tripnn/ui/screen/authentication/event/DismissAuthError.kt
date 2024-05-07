package ru.nn.tripnn.ui.screen.authentication.event

sealed class DismissAuthError {
    data object PasswordError: DismissAuthError()
    data object EmailError: DismissAuthError()
    data object UserNameError: DismissAuthError()
}