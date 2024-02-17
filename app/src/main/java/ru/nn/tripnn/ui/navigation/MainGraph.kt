package ru.nn.tripnn.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.nn.tripnn.ui.screen.GeneralUiViewModel
import ru.nn.tripnn.ui.screen.application.account.AccountScreen
import ru.nn.tripnn.ui.screen.application.account.AccountViewModel
import ru.nn.tripnn.ui.screen.application.home.HomeScreen
import ru.nn.tripnn.ui.screen.application.home.HomeViewModel
import ru.nn.tripnn.ui.screen.application.settings.SettingsScreen
import ru.nn.tripnn.ui.screen.getIsoLang
import ru.nn.tripnn.ui.util.changeLocales

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
    PLACE_INFO("place_info_route"),
    CUR_ROUTE("cur_route_route")
}

fun NavGraphBuilder.addAppGraph(
    generalUiViewModel: GeneralUiViewModel,
    navController: NavController,
    navigateTo: (String) -> Unit,
    onBack: () -> Unit,
    onLeaveAccount: () -> Unit
) {
    navigation(
        route = MAIN_GRAPH_ROUTE,
        startDestination = AppRoutes.HOME.route
    ) {
        composable(
            AppRoutes.HOME.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
        ) { backStackEntry ->
            val graphBack =
                remember(backStackEntry) { navController.getBackStackEntry(MAIN_GRAPH_ROUTE) }
            val homeViewModel = hiltViewModel<HomeViewModel>(graphBack)

            HomeScreen(
                homeScreenState = homeViewModel.homeScreenState,
                onAccountClick = { navigateTo(AppRoutes.ACCOUNT.route) },
                onHistoryClick = { /*navigateTo(AppRoutes.HISTORY.route)*/ },
                onFavouriteClick = { /*navigateTo(AppRoutes.FAVOURITE.route)*/ },
                onSettingsClick = { navigateTo(AppRoutes.SETTINGS.route) },
                onAllRoutesClick = { /*navigateTo(AppRoutes.ALL_ROUTES.route)*/ },
                onRouteCardClick = { /*navigateTo(AppRoutes.ROUTE_INFO.route)*/ },
                onNewRouteClick = { /*navigateTo(AppRoutes.NEW_ROUTE.route)*/ },
                onCurRouteClick = { /*navigateTo(AppRoutes.CUR_ROUTE.route)*/ }
            )
        }

        composable(
            route = AppRoutes.SETTINGS.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            val preferences = generalUiViewModel.uiPreferencesState
            val context = LocalContext.current
            SettingsScreen(
                onBackClick = onBack,
                onThemeChange = generalUiViewModel::changeTheme,
                onLanguageChange = {
                    generalUiViewModel.changeLanguage(it)
                    changeLocales(context, getIsoLang(it))
                },
                onCurrencyChange = generalUiViewModel::changeCurrency,
                currentTheme = preferences.theme,
                currentLanguage = preferences.language,
                currentCurrency = preferences.currency
            )
        }

        composable(AppRoutes.ACCOUNT.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val graphBack =
                remember(backStackEntry) { navController.getBackStackEntry(MAIN_GRAPH_ROUTE) }
            val accountViewModel = hiltViewModel<AccountViewModel>(graphBack)
            AccountScreen(
                onBackClick = onBack,
                userState = accountViewModel.userState,
                onUserNameChange = accountViewModel::changeUserName,
                onClearHistory = accountViewModel::clearHistory,
                onDeleteAccount = accountViewModel::deleteAccount,
                onAvatarChange = accountViewModel::avatarChange,
                onLeaveAccount = onLeaveAccount
            )
        }
    }
}