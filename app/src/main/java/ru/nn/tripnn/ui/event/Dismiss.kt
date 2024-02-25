package ru.nn.tripnn.ui.event

sealed class Dismiss {
    data object PasswordError: Dismiss()
    data object EmailError: Dismiss()
    data object UserNameError: Dismiss()
}