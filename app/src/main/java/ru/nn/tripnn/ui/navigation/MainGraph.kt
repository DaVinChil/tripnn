package ru.nn.tripnn.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.nn.tripnn.ui.screen.main.settings.UserSettingsViewModel
import ru.nn.tripnn.ui.screen.main.settings.getIsoLang
import ru.nn.tripnn.ui.screen.main.account.AccountScreen
import ru.nn.tripnn.ui.screen.main.account.AccountViewModel
import ru.nn.tripnn.ui.screen.main.constructor.ConstructorScreen
import ru.nn.tripnn.ui.screen.main.favourite.FavouriteScreen
import ru.nn.tripnn.ui.screen.main.favourite.FavouriteViewModel
import ru.nn.tripnn.ui.screen.main.history.HistoryScreen
import ru.nn.tripnn.ui.screen.main.history.HistoryViewModel
import ru.nn.tripnn.ui.screen.main.home.HomeScreen
import ru.nn.tripnn.ui.screen.main.home.HomeViewModel
import ru.nn.tripnn.ui.screen.main.photos.PhotosScreen
import ru.nn.tripnn.ui.screen.main.photos.PhotosViewModel
import ru.nn.tripnn.ui.screen.main.recommendations.RecommendationsScreen
import ru.nn.tripnn.ui.screen.main.recommendations.RecommendationsViewModel
import ru.nn.tripnn.ui.screen.main.settings.SettingsScreen
import ru.nn.tripnn.ui.util.changeLocales

enum class AppRoutes(
    val route: String
) {
    HOME("home_route"),
    SETTINGS("settings_route"),
    ACCOUNT("account_route"),
    FAVOURITE("favourite_route"),
    HISTORY("history_route"),
    RECOMMENDED_ROUTES("all_routes_route"),
    NEW_ROUTE("new_route_route"),
    CUR_ROUTE("cur_route_route"),
    PHOTOS_ROUTE("photos_route")
}

fun NavGraphBuilder.addAppGraph(
    userSettingsViewModel: UserSettingsViewModel,
    navController: NavController,
    navigateTo: (String) -> Unit,
    onBack: () -> Unit,
    onLeaveAccount: () -> Unit,
    navigateToPhotos: (String, Int) -> Unit
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
                if (initialState.destination.route != AppRoutes.PHOTOS_ROUTE.route) {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300)
                    )
                }

                EnterTransition.None
            },
        ) { backStackEntry ->
            val graphBack =
                remember(backStackEntry) { navController.getBackStackEntry(MAIN_GRAPH_ROUTE) }
            val homeViewModel = hiltViewModel<HomeViewModel>(graphBack)

            HomeScreen(
                currentRoute = homeViewModel.currentRoute,
                recommendedRoutes = homeViewModel.recommendedRoutes,
                onAccountClick = { navigateTo(AppRoutes.ACCOUNT.route) },
                onHistoryClick = { navigateTo(AppRoutes.HISTORY.route) },
                onFavouriteClick = { navigateTo(AppRoutes.FAVOURITE.route) },
                onSettingsClick = { navigateTo(AppRoutes.SETTINGS.route) },
                onAllRoutesClick = { navigateTo(AppRoutes.RECOMMENDED_ROUTES.route) },
                onNewRouteClick = { navigateTo(AppRoutes.NEW_ROUTE.route) },
                onCurrentRouteClick = { /*navigateTo(AppRoutes.CUR_ROUTE.route)*/ },
                removeRouteFromFavourite = homeViewModel::removeRouteFromFavourite,
                addRouteToFavourite = homeViewModel::addRouteToFavourite,
                removePlaceFromFavourite = homeViewModel::removePlaceFromFavourite,
                addPlaceToFavourite = homeViewModel::addPlaceToFavourite,
                toPhotos = navigateToPhotos
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
            val preferences = userSettingsViewModel.userSettingsState
            val context = LocalContext.current
            SettingsScreen(
                onBackClick = onBack,
                onThemeChange = userSettingsViewModel::changeTheme,
                onLanguageChange = {
                    userSettingsViewModel.changeLanguage(it)
                    changeLocales(context, getIsoLang(it))
                },
                onCurrencyChange = userSettingsViewModel::changeCurrency,
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

            LaunchedEffect(false) {
                accountViewModel.init()
            }

            AccountScreen(
                onBackClick = onBack,
                userInfo = accountViewModel.userInfo,
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
        ) { backStackEntry ->
            val graphBack =
                remember(backStackEntry) { navController.getBackStackEntry(MAIN_GRAPH_ROUTE) }
            val favouriteViewModel = hiltViewModel<FavouriteViewModel>(graphBack)

            LaunchedEffect(true) {
                favouriteViewModel.init()
            }

            FavouriteScreen(
                filterPlaces = favouriteViewModel::filterPlaces,
                filterRoutes = favouriteViewModel::filterRoutes,
                favouritePlaces = favouriteViewModel.favouritePlaces,
                favouriteRoutes = favouriteViewModel.favouriteRoutes,
                removePlaceFromFavourite = favouriteViewModel::removePlaceFromFavourite,
                removeRouteFromFavourite = favouriteViewModel::removeRouteFromFavourite,
                addPlaceToFavourite = favouriteViewModel::addPlaceToFavourite,
                addRouteToFavourite = favouriteViewModel::addRouteToFavourite,
                onBack = onBack,
                onTakeTheRoute = { TODO() },
                toPhotos = navigateToPhotos
            )
        }

        composable(
            route = AppRoutes.RECOMMENDED_ROUTES.route,
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
            val recommendationsViewModel = hiltViewModel<RecommendationsViewModel>(graphBack)

            LaunchedEffect(false) {
                recommendationsViewModel.init()
            }

            RecommendationsScreen(
                onBack = onBack,
                filterRoutes = recommendationsViewModel::filterRoutes,
                routes = recommendationsViewModel.recommendedRoutes,
                removeRouteFromFavourite = recommendationsViewModel::removeRouteFromFavourite,
                addRouteToFavourite = recommendationsViewModel::addRouteToFavourite,
                removePlaceFromFavourite = recommendationsViewModel::removePlaceFromFavourite,
                addPlaceToFavourite = recommendationsViewModel::addPlaceToFavourite,
                isEmpty = recommendationsViewModel.isEmpty,
                toPhotos = navigateToPhotos
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
        ) { backStackEntry ->
            val graphBack =
                remember(backStackEntry) { navController.getBackStackEntry(MAIN_GRAPH_ROUTE) }
            val homeViewModel = hiltViewModel<HistoryViewModel>(graphBack)

            LaunchedEffect(true) {
                homeViewModel.init()
            }

            HistoryScreen(
                filterPlaces = homeViewModel::filterPlaces,
                filterRoutes = homeViewModel::filterRoutes,
                places = homeViewModel.visitedPlaces,
                routes = homeViewModel.takenRoutes,
                removePlaceFromFavourite = homeViewModel::removePlaceFromFavourite,
                removeRouteFromFavourite = homeViewModel::removeRouteFromFavourite,
                addPlaceToFavourite = homeViewModel::addPlaceToFavourite,
                addRouteToFavourite = homeViewModel::addRouteToFavourite,
                onBack = onBack,
                toPhotos = navigateToPhotos,
                onTakeTheRoute = { TODO() }
            )
        }

        composable(
            route = AppRoutes.NEW_ROUTE.route,
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

            ConstructorScreen(onBack = onBack, onContinue = {})
        }

        composable(
            route = AppRoutes.PHOTOS_ROUTE.route + "/{placeId}/{initial}",
            arguments = listOf(
                navArgument("placeId") { type = NavType.StringType },
                navArgument("initial") { type = NavType.IntType }
            ),
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val graphBack =
                remember(backStackEntry) { navController.getBackStackEntry(MAIN_GRAPH_ROUTE) }
            val photosViewModel = hiltViewModel<PhotosViewModel>(graphBack)

            LaunchedEffect(Unit) {
                photosViewModel.init(backStackEntry.arguments?.getString("placeId") ?: "")
            }

            PhotosScreen(
                photos = photosViewModel.photos,
                initialPhoto = backStackEntry.arguments?.getInt("initial") ?: 0,
                onClose = onBack
            )
        }
    }
}