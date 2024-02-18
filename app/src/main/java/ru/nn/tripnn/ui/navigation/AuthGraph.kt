package ru.nn.tripnn.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.nn.tripnn.domain.entity.Credentials
import ru.nn.tripnn.ui.screen.authentication.LogInScreen
import ru.nn.tripnn.ui.screen.authentication.RegistrationScreen

enum class AuthRoutes(
    val route: String
) {
    REGISTRATION("registration"),
    LOGIN("login"),
    AUTHENTICATION(AUTH_GRAPH_ROUTE)
}

fun NavGraphBuilder.addAuthGraph(
    navigateTo: (String) -> Unit,
    onSignUp: (Credentials) -> Unit,
    onLogIn: (rememberMe: Boolean, credentials: Credentials) -> Unit,
    onForgot: () -> Unit
) {
    navigation(
        route = AUTH_GRAPH_ROUTE,
        startDestination = AuthRoutes.REGISTRATION.route
    ) {
        composable(AuthRoutes.REGISTRATION.route) {
            RegistrationScreen(
                onSignInClick = { navigateTo(AuthRoutes.LOGIN.route) },
                onSignUpClick = onSignUp
            )
        }

        composable(AuthRoutes.LOGIN.route) {
            LogInScreen(
                onForgotClick = onForgot,
                onLogInClick = onLogIn,
                onRegisterClick = { navigateTo(AuthRoutes.REGISTRATION.route) }
            )
        }
    }
}