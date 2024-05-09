package ru.nn.tripnn.ui.screen.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.nn.tripnn.data.local.usersettings.getIsoLang
import ru.nn.tripnn.ui.screen.authentication.AuthenticationViewModel
import ru.nn.tripnn.ui.screen.main.account.AccountScreen
import ru.nn.tripnn.ui.screen.main.account.AccountViewModel
import ru.nn.tripnn.ui.screen.main.constructor.ConstructorScreen
import ru.nn.tripnn.ui.screen.main.constructor.ConstructorViewModel
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
import ru.nn.tripnn.ui.screen.main.settings.UserSettingsViewModel
import ru.nn.tripnn.ui.screen.main.takingroute.TakingTheRouteScreen
import ru.nn.tripnn.ui.util.changeLocales
import ru.nn.tripnn.ui.common.fullSlideInHorizontal
import ru.nn.tripnn.ui.common.fullSlideInVertical
import ru.nn.tripnn.ui.common.fullSlideOutHorizontal
import ru.nn.tripnn.ui.common.fullSlideOutVertical

enum class AppRoutes(
    val route: String
) {
    HOME("home_route"),
    SETTINGS("settings_route"),
    ACCOUNT("account_route"),
    FAVOURITE("favourite_route"),
    HISTORY("history_route"),
    RECOMMENDED_ROUTES("all_routes_route"),
    CONSTRUCTOR_ROUTE("constructor_route"),
    CUR_ROUTE("cur_route_route"),
    PHOTOS_ROUTE("photos_route");

    companion object {
        const val MAIN_GRAPH_ROUTE = "application"
    }
}

fun NavGraphBuilder.addAppGraph(
    tripNnNavController: TripNnNavController
) {
    val navController = tripNnNavController.navController
    val navigateTo = tripNnNavController::navigateTo
    val onBack = tripNnNavController::upPress
    val navigateToPhotos = { placeId: String, initial: Int ->
        navController.navigate(AppRoutes.PHOTOS_ROUTE.route + "/$placeId/$initial") {
            launchSingleTop = true
            restoreState = true
        }
    }

    navigation(
        route = AppRoutes.MAIN_GRAPH_ROUTE,
        startDestination = AppRoutes.HOME.route
    ) {
        home(navigateToPhotos, navigateTo)
        userSettings(onBack)
        account(onBack, navigateTo)
        favourites(onBack, navigateToPhotos, navigateTo)
        recommendations(onBack, navigateToPhotos, navigateTo)
        history(onBack, navigateToPhotos, navigateTo)
        constructor(onBack, navigateToPhotos)
        photos(onBack)
        takingTheRoute()
    }
}

fun NavGraphBuilder.home(
    navigateToPhotos: (String, Int) -> Unit,
    navigateTo: (String) -> Unit
) {
    composable(
        route = AppRoutes.HOME.route,
        exitTransition = fullSlideOutHorizontal(toRight = false),
        popEnterTransition = fullSlideInHorizontal(fromRight = false),
    ) {
        val homeViewModel = hiltViewModel<HomeViewModel>()

        LaunchedEffect(Unit) {
            homeViewModel.init()
        }

        HomeScreen(
            currentRoute = homeViewModel.currentRoute,
            recommendedRoutes = homeViewModel.recommendedRoutes,
            onAccountClick = { navigateTo(AppRoutes.ACCOUNT.route) },
            onHistoryClick = { navigateTo(AppRoutes.HISTORY.route) },
            onFavouriteClick = { navigateTo(AppRoutes.FAVOURITE.route) },
            onSettingsClick = { navigateTo(AppRoutes.SETTINGS.route) },
            onAllRoutesClick = { navigateTo(AppRoutes.RECOMMENDED_ROUTES.route) },
            onNewRouteClick = {
                if (homeViewModel.isRouteInProgress()) {
                    homeViewModel.deleteCurrentRoute()
                }
                navigateTo(AppRoutes.CONSTRUCTOR_ROUTE.route)
            },
            onTakeTheRoute = {
                homeViewModel.setCurrentRoute(it)
                navigateTo(AppRoutes.CUR_ROUTE.route)
            },
            onCurrentRouteClick = {
                if (homeViewModel.isRouteInProgress()) {
                    navigateTo(AppRoutes.CUR_ROUTE.route)
                }
            },
            removeRouteFromFavourite = homeViewModel::removeRouteFromFavourite,
            addRouteToFavourite = homeViewModel::addRouteToFavourite,
            removePlaceFromFavourite = homeViewModel::removePlaceFromFavourite,
            addPlaceToFavourite = homeViewModel::addPlaceToFavourite,
            toPhotos = navigateToPhotos
        )
    }
}

fun NavGraphBuilder.userSettings(
    onBack: () -> Unit
) {
    composable(
        route = AppRoutes.SETTINGS.route,
        enterTransition = fullSlideInHorizontal(),
        exitTransition = fullSlideOutHorizontal()
    ) {
        val userSettingsViewModel = hiltViewModel<UserSettingsViewModel>()

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
}

fun NavGraphBuilder.account(
    onBack: () -> Unit,
    navigateTo: (String) -> Unit
) {
    composable(
        route = AppRoutes.ACCOUNT.route,
        enterTransition = fullSlideInHorizontal(),
        exitTransition = fullSlideOutHorizontal()
    ) {
        val accountViewModel = hiltViewModel<AccountViewModel>()
        val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()

        LaunchedEffect(false) {
            accountViewModel.init()
        }

        AccountScreen(
            onBackClick = onBack,
            userInfoData = accountViewModel.userInfoData,
            onUserNameChange = accountViewModel::changeUserName,
            onClearHistory = accountViewModel::clearHistory,
            onDeleteAccount = accountViewModel::deleteAccount,
            onAvatarChange = accountViewModel::avatarChange,
            onLeaveAccount = {
                authenticationViewModel.logout()
                navigateTo(AuthRoutes.AUTH_GRAPH_ROUTE)
            }
        )
    }
}

fun NavGraphBuilder.favourites(
    onBack: () -> Unit,
    navigateToPhotos: (String, Int) -> Unit,
    navigateTo: (String) -> Unit
) {
    composable(
        route = AppRoutes.FAVOURITE.route,
        enterTransition = fullSlideInHorizontal(),
        exitTransition = fullSlideOutHorizontal()
    ) {
        val favouriteViewModel = hiltViewModel<FavouriteViewModel>()

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
            onTakeTheRoute = {
                favouriteViewModel.setCurrentRoute(it)
                navigateTo(AppRoutes.CUR_ROUTE.route)
            },
            toPhotos = navigateToPhotos,
            alreadyHasRoute = favouriteViewModel.hasCurrentRoute
        )
    }
}

fun NavGraphBuilder.recommendations(
    onBack: () -> Unit,
    navigateToPhotos: (String, Int) -> Unit,
    navigateTo: (String) -> Unit
) {
    composable(
        route = AppRoutes.RECOMMENDED_ROUTES.route,
        enterTransition = fullSlideInHorizontal(),
        exitTransition = fullSlideOutHorizontal()
    ) {
        val recommendationsViewModel = hiltViewModel<RecommendationsViewModel>()

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
            toPhotos = navigateToPhotos,
            alreadyHasRoute = recommendationsViewModel.hasCurrentRoute,
            onTakeTheRoute = {
                recommendationsViewModel.setCurrentRoute(it)
                navigateTo(AppRoutes.CUR_ROUTE.route)
            }
        )
    }
}

fun NavGraphBuilder.history(
    onBack: () -> Unit,
    navigateToPhotos: (String, Int) -> Unit,
    navigateTo: (String) -> Unit
) {
    composable(
        route = AppRoutes.HISTORY.route,
        enterTransition = fullSlideInHorizontal(),
        exitTransition = fullSlideOutHorizontal()
    ) {
        val historyViewModel = hiltViewModel<HistoryViewModel>()

        LaunchedEffect(true) {
            historyViewModel.init()
        }

        HistoryScreen(
            filterPlaces = historyViewModel::filterPlaces,
            filterRoutes = historyViewModel::filterRoutes,
            places = historyViewModel.visitedPlaces,
            routes = historyViewModel.takenRoutes,
            removePlaceFromFavourite = historyViewModel::removePlaceFromFavourite,
            removeRouteFromFavourite = historyViewModel::removeRouteFromFavourite,
            addPlaceToFavourite = historyViewModel::addPlaceToFavourite,
            addRouteToFavourite = historyViewModel::addRouteToFavourite,
            onBack = onBack,
            toPhotos = navigateToPhotos,
            onTakeTheRoute = {
                historyViewModel.setCurrentRoute(it)
                navigateTo(AppRoutes.CUR_ROUTE.route)
            },
            alreadyHasRoute = historyViewModel.hasCurrentRoute
        )
    }
}

fun NavGraphBuilder.constructor(
    onBack: () -> Unit,
    navigateToPhotos: (String, Int) -> Unit
) {
    composable(
        route = AppRoutes.CONSTRUCTOR_ROUTE.route,
        enterTransition = fullSlideInHorizontal(),
        exitTransition = fullSlideOutHorizontal()
    ) {
        val constructorViewModel = hiltViewModel<ConstructorViewModel>()

        LaunchedEffect(Unit) {
            constructorViewModel.init()
        }

        ConstructorScreen(
            onBack = onBack,
            currentRoute = constructorViewModel.currentRouteState,
            addPlace = constructorViewModel::addPlace,
            removePlaceFromRoute = constructorViewModel::removePlace,
            takeRoute = {
                constructorViewModel.finishConstructing()
            },
            toPhotos = navigateToPhotos,
            removePlaceFromFavourite = constructorViewModel::removePlaceFromFavourite,
            addPlaceToFavourite = constructorViewModel::addPlaceToFavourite
        )
    }
}

fun NavGraphBuilder.photos(onBack: () -> Unit) {
    composable(
        route = AppRoutes.PHOTOS_ROUTE.route + "/{placeId}/{initial}",
        arguments = listOf(
            navArgument("placeId") { type = NavType.StringType },
            navArgument("initial") { type = NavType.IntType }
        ),
        enterTransition = fullSlideInVertical(),
        exitTransition = fullSlideOutVertical()
    ) { backStackEntry ->
        val photosViewModel = hiltViewModel<PhotosViewModel>()

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

fun NavGraphBuilder.takingTheRoute() {
    composable(
        route = AppRoutes.CUR_ROUTE.route,
        enterTransition = fullSlideInVertical(),
        exitTransition = fullSlideOutVertical()
    ) {
        TakingTheRouteScreen()
    }
}