package ru.nn.tripnn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

const val MAIN_GRAPH_ROUTE = "application"
const val AUTH_GRAPH_ROUTE = "authentication"
const val SPLASH_ROUTE = "splash"

val authRoutes = authRoutesString()
fun authRoutesString(): Set<String> = AuthRoutes.entries.map { it.route }.toSet()

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
        } else if(currentRoute == SPLASH_ROUTE) {
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
                popUpTo(AUTH_GRAPH_ROUTE) {
                    inclusive = true
                }
            } else if (currentRoute == SPLASH_ROUTE) {
                popUpTo(SPLASH_ROUTE) {
                    inclusive = true
                }
            } else {
                popUpTo(MAIN_GRAPH_ROUTE) {
                    inclusive = true
                }
            }

        }
    }
}

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}
