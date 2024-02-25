package ru.nn.tripnn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.nn.tripnn.ui.screen.AuthenticationViewModel
import ru.nn.tripnn.ui.screen.authentication.LogInScreen
import ru.nn.tripnn.ui.screen.authentication.LogInViewModel
import ru.nn.tripnn.ui.screen.authentication.RegistrationScreen
import ru.nn.tripnn.ui.screen.authentication.RegistrationViewModel

enum class AuthRoutes(
    val route: String
) {
    REGISTRATION("registration"),
    LOGIN("login"),
    AUTHENTICATION(AUTH_GRAPH_ROUTE)
}

fun NavGraphBuilder.addAuthGraph(
    navigateTo: (String) -> Unit,
    navController: TripNnNavController,
    authViewModel: AuthenticationViewModel
) {
    navigation(
        route = AUTH_GRAPH_ROUTE,
        startDestination = AuthRoutes.REGISTRATION.route
    ) {
        composable(AuthRoutes.REGISTRATION.route) { backStackEntry ->
            val registrationViewModel = backStackEntry.getViewModel<RegistrationViewModel>(
                route = AUTH_GRAPH_ROUTE,
                navController = navController.navController
            )

            if (registrationViewModel.authenticated.value == true) {
                LaunchedEffect(Unit) {
                    navigateTo(MAIN_GRAPH_ROUTE)
                }
            }

            RegistrationScreen(
                onSignInClick = { navController.navigateTo(AuthRoutes.LOGIN.route) },
                authenticate = registrationViewModel::authenticate,
                dismissError = registrationViewModel::dismissError,
                authenticated = registrationViewModel.authenticated,
                emailState = registrationViewModel.emailState,
                passwordState = registrationViewModel.passwordState,
                userNameState = registrationViewModel.userNameState
            )
        }

        composable(AuthRoutes.LOGIN.route) { backStackEntry ->
            val logInViewModel = backStackEntry.getViewModel<LogInViewModel>(
                route = AUTH_GRAPH_ROUTE,
                navController = navController.navController
            )

            if (logInViewModel.authenticated.value == true) {
                LaunchedEffect(Unit) {
                    navigateTo(MAIN_GRAPH_ROUTE)
                }
            }

            LogInScreen(
                onForgotClick = { },
                authenticate = logInViewModel::authenticate,
                authenticated = logInViewModel.authenticated,
                onRegisterClick = { navigateTo(AuthRoutes.REGISTRATION.route) },
                dismissError = logInViewModel::dismissError,
                emailState = logInViewModel.emailState,
                passwordState = logInViewModel.passwordState
            )
        }
    }
}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.getViewModel(route: String, navController: NavController): T {
    val graphBack =
        remember(this) {
            navController.getBackStackEntry(
                AUTH_GRAPH_ROUTE
            )
        }
    return hiltViewModel<T>(graphBack)
}