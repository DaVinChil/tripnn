package ru.nn.tripnn.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.nn.tripnn.ui.screen.authentication.LoginScreen
import ru.nn.tripnn.ui.screen.authentication.LoginViewModel
import ru.nn.tripnn.ui.screen.authentication.RegistrationScreen
import ru.nn.tripnn.ui.screen.authentication.RegistrationViewModel

enum class AuthRoutes(
    val route: String
) {
    REGISTRATION("registration"),
    LOGIN("login");

    companion object {
        const val AUTH_GRAPH_ROUTE = "authentication"
    }
}

fun NavGraphBuilder.addAuthGraph(
    navController: TripNnNavController
) {
    val navigateTo = navController::navigateTo

    navigation(
        route = AuthRoutes.AUTH_GRAPH_ROUTE,
        startDestination = AuthRoutes.REGISTRATION.route
    ) {
        registration(navigateTo)
        login(navigateTo)
    }
}

fun NavGraphBuilder.registration(navigateTo: (String) -> Unit) {
    composable(AuthRoutes.REGISTRATION.route) {
        val registrationViewModel = hiltViewModel<RegistrationViewModel>()

        RegistrationScreen(
            onSignInClick = { navigateTo(AuthRoutes.LOGIN.route) },
            authenticate = { rememberMe, email, userName, password, confirmPassword ->
                registrationViewModel.authenticate(rememberMe, email, userName, password, confirmPassword) {
                    navigateTo(AppRoutes.MAIN_GRAPH_ROUTE)
                }
            },
            dismissError = registrationViewModel::dismissError,
            authenticated = registrationViewModel.authenticated,
            emailState = registrationViewModel.emailState,
            passwordState = registrationViewModel.passwordState,
            userNameState = registrationViewModel.userNameState
        )
    }
}

fun NavGraphBuilder.login(navigateTo: (String) -> Unit) {
    composable(AuthRoutes.LOGIN.route) {
        val logInViewModel = hiltViewModel<LoginViewModel>()

        LoginScreen(
            onForgotClick = { },
            authenticate = { rememberMe, email, password ->
                logInViewModel.authenticate(rememberMe, email, password) {
                    navigateTo(AppRoutes.MAIN_GRAPH_ROUTE)
                }
            },
            authenticated = logInViewModel.authenticated,
            onRegisterClick = { navigateTo(AuthRoutes.REGISTRATION.route) },
            dismissError = logInViewModel::dismissError,
            emailState = logInViewModel.emailState,
            passwordState = logInViewModel.passwordState
        )
    }
}