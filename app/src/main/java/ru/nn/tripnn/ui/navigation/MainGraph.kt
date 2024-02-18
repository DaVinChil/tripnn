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
import ru.nn.tripnn.ui.screen.main.account.AccountScreen
import ru.nn.tripnn.ui.screen.main.account.AccountViewModel
import ru.nn.tripnn.ui.screen.main.favourite.FavouriteScreen
import ru.nn.tripnn.ui.screen.main.favourite.FavouriteViewModel
import ru.nn.tripnn.ui.screen.main.history.HistoryScreen
import ru.nn.tripnn.ui.screen.main.history.HistoryViewModel
import ru.nn.tripnn.ui.screen.main.recommendations.RecommendationsScreen
import ru.nn.tripnn.ui.screen.main.home.HomeScreen
import ru.nn.tripnn.ui.screen.main.home.HomeViewModel
import ru.nn.tripnn.ui.screen.main.recommendations.RecommendationsViewModel
import ru.nn.tripnn.ui.screen.main.settings.SettingsScreen
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
            route = AppRoutes.HOME.route,
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
                onHistoryClick = { navigateTo(AppRoutes.HISTORY.route) },
                onFavouriteClick = { navigateTo(AppRoutes.FAVOURITE.route) },
                onSettingsClick = { navigateTo(AppRoutes.SETTINGS.route) },
                onAllRoutesClick = { navigateTo(AppRoutes.ALL_ROUTES.route) },
                onNewRouteClick = { /*navigateTo(AppRoutes.NEW_ROUTE.route)*/ },
                onCurRouteClick = { /*navigateTo(AppRoutes.CUR_ROUTE.route)*/ },
                routeFull = homeViewModel.routeFull,
                placeFull = homeViewModel.placeFull,
                removeRouteFromFavourite = homeViewModel::removeRouteFromFavourite,
                addRouteToFavourite = homeViewModel::addRouteToFavourite,
                removePlaceFromFavourite = homeViewModel::removePlaceFromFavourite,
                addPlaceToFavourite = homeViewModel::addPlaceToFavourite,
                getPlaceFullInfo = homeViewModel::getFullPlaceInfo,
                getRouteFullInfo = homeViewModel::getFullRouteInfo
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

        composable(
            route = AppRoutes.ACCOUNT.route,
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

        composable(
            route = AppRoutes.FAVOURITE.route,
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
        ) {backStackEntry ->
            val graphBack =
                remember(backStackEntry) { navController.getBackStackEntry(MAIN_GRAPH_ROUTE) }
            val favouriteViewModel = hiltViewModel<FavouriteViewModel>(graphBack)
            FavouriteScreen(
                filterPlaces = favouriteViewModel::filterPlaces,
                filterRoutes = favouriteViewModel::filterRoutes,
                isLoading = favouriteViewModel.isLoading,
                favouritePlaces = favouriteViewModel.favouritePlaces,
                favouriteRoutes = favouriteViewModel.favouriteRoutes,
                placeFull = favouriteViewModel.placeFull,
                routeFull = favouriteViewModel.routeFull,
                getPlaceFullInfo = favouriteViewModel::getFullPlaceInfo,
                getRouteFullInfo = favouriteViewModel::getFullRouteInfo,
                removePlaceFromFavourite = favouriteViewModel::removePlaceFromFavourite,
                removeRouteFromFavourite = favouriteViewModel::removeRouteFromFavourite,
                addPlaceToFavourite = favouriteViewModel::addPlaceToFavourite,
                addRouteToFavourite = favouriteViewModel::addRouteToFavourite,
                onBack = onBack
            )
        }

        composable(
            route = AppRoutes.ALL_ROUTES.route,
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
            val homeViewModel = hiltViewModel<RecommendationsViewModel>(graphBack)

            RecommendationsScreen(
                onBack = onBack,
                filterRoutes = homeViewModel::filterRoutes,
                routes = homeViewModel.recommendedRoutes,
                routeFull = homeViewModel.routeFull,
                placeFull = homeViewModel.placeFull,
                getRouteFullInfo = homeViewModel::getFullRouteInfo,
                getPlaceFullInfo = homeViewModel::getFullPlaceInfo,
                removeRouteFromFavourite = homeViewModel::removeRouteFromFavourite,
                addRouteToFavourite = homeViewModel::addRouteToFavourite,
                removePlaceFromFavourite = homeViewModel::removePlaceFromFavourite,
                addPlaceToFavourite = homeViewModel::addPlaceToFavourite
            )
        }

        composable(
            route = AppRoutes.HISTORY.route,
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
        ) {backStackEntry ->
            val graphBack =
                remember(backStackEntry) { navController.getBackStackEntry(MAIN_GRAPH_ROUTE) }
            val homeViewModel = hiltViewModel<HistoryViewModel>(graphBack)

            HistoryScreen(
                filterPlaces = homeViewModel::filterPlaces,
                filterRoutes = homeViewModel::filterRoutes,
                isLoading = homeViewModel.isLoading,
                places = homeViewModel.visitedPlaces,
                routes = homeViewModel.takenRoutes,
                placeFull = homeViewModel.placeFull,
                routeFull = homeViewModel.routeFull,
                getPlaceFullInfo = homeViewModel::getFullPlaceInfo,
                getRouteFullInfo = homeViewModel::getFullRouteInfo,
                removePlaceFromFavourite = homeViewModel::removePlaceFromFavourite,
                removeRouteFromFavourite = homeViewModel::removeRouteFromFavourite,
                addPlaceToFavourite = homeViewModel::addPlaceToFavourite,
                addRouteToFavourite = homeViewModel::addRouteToFavourite,
                onBack = onBack
            )
        }
    }
}