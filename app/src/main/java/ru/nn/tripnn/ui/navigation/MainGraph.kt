package ru.nn.tripnn.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.nn.tripnn.ui.screen.MainViewModel
import ru.nn.tripnn.ui.screen.application.SettingsScreen
import ru.nn.tripnn.ui.screen.application.account.AccountScreen
import ru.nn.tripnn.ui.screen.application.home.HomeScreen

enum class AppRoutes(
    val route: String
) {
    HOME("home_route"),
    SETTINGS("settings_route"),
    ACCOUNT("account_route"),
    FAVOURITE("favourite_route"),
    HISTORY("history_route"),
    ALL_ROUTES("all_routes_route"),
    NEW_ROUTE("new_route_route"),
    ROUTE_INFO("route_info_route"),
    ALL_PLACES("all_places_route"),
    PLACE_INFO("place_info_route"),
    CUR_ROUTE("cur_route_route")
}

fun NavGraphBuilder.addAppGraph(
    navigateTo: (String) -> Unit,
    onBack: () -> Unit,
    onThemeChange: (Int) -> Unit,
    onLeaveAccount: () -> Unit,
    viewModel: MainViewModel,
    onLanguageChange: (Int) -> Unit,
    onCurrencyChange: (Int) -> Unit,
    currentTheme: Int,
    currentLanguage: Int,
    currentCurrency: Int
) {
    navigation(
        route = MAIN_GRAPH_ROUTE,
        startDestination = AppRoutes.HOME.route
    ) {
        composable(AppRoutes.HOME.route) {

            HomeScreen(
                homeScreenState  = viewModel.homeScreenState,
                onAccountClick   = { navigateTo(AppRoutes.ACCOUNT.route)    },
                onHistoryClick   = { navigateTo(AppRoutes.HISTORY.route)    },
                onFavouriteClick = { navigateTo(AppRoutes.FAVOURITE.route)  },
                onSettingsClick  = { navigateTo(AppRoutes.SETTINGS.route)   },
                onAllRoutesClick = { navigateTo(AppRoutes.ALL_ROUTES.route) },
                onRouteCardClick = { navigateTo(AppRoutes.ROUTE_INFO.route) },
                onAllPlacesClick = { navigateTo(AppRoutes.ALL_PLACES.route) },
                onNewRouteClick  = { navigateTo(AppRoutes.NEW_ROUTE.route)  },
                onCurRouteClick  = { navigateTo(AppRoutes.CUR_ROUTE.route)  }
            )
        }

        composable(AppRoutes.SETTINGS.route) {
            SettingsScreen(
                onBackClick = onBack,
                onThemeChange = onThemeChange,
                onLanguageChange = onLanguageChange,
                onCurrencyChange = onCurrencyChange,
                currentTheme = currentTheme,
                currentLanguage = currentLanguage,
                currentCurrency = currentCurrency
            )
        }

        composable(AppRoutes.ACCOUNT.route) {
            println("HELLO")
            AccountScreen(
                onBackClick = onBack,
                userState = viewModel.userState,
                onUserNameChange = viewModel::changeUserName,
                onClearHistory = viewModel::clearHistory,
                onDeleteAccount = viewModel::deleteAccount,
                onAvatarChange = viewModel::avatarChange,
                onLeaveAccount = onLeaveAccount
            )
        }
    }
}