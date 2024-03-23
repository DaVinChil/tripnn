package ru.nn.tripnn.ui.screen.authentication.event

sealed class Dismiss {
    data object PasswordError: Dismiss()
    data object EmailError: Dismiss()
    data object UserNameError: Dismiss()
}