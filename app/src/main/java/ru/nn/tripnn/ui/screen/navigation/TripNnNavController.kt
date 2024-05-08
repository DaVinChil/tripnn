package ru.nn.tripnn.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

val authRoutes = AuthRoutes.entries.map(AuthRoutes::route).toSet()

@Composable
fun rememberTripNnNavController(
    navController: NavHostController = rememberNavController(),
): TripNnNavController = remember(navController) {
    TripNnNavController(navController)
}

class TripNnNavController(
    val navController: NavHostController
) {
    private val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.popBackStack()
    }

    fun navigateTo(route: String) {
        if (route == currentRoute)
            return

        if (authRoutes.contains(route) xor authRoutes.contains(currentRoute)) {
            navigateToAndPopAll(route)
        } else {
            navController.navigate(route) {
                launchSingleTop = true
                popUpTo(route) {
                    inclusive = true
                }
            }
        }
    }

    private fun navigateToAndPopAll(route: String) {
        navController.navigate(route) {
            launchSingleTop = true

            if (authRoutes.contains(currentRoute)) {
                popUpTo(AuthRoutes.AUTH_GRAPH_ROUTE) {
                    inclusive = true
                }
            } else {
                popUpTo(AppRoutes.MAIN_GRAPH_ROUTE) {
                    inclusive = true
                }
            }

        }
    }
}

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)
