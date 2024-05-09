package ru.nn.tripnn.ui.screen.main.home

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.ROUTES
import ru.nn.tripnn.data.stub_data.ROUTE_1
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.common.DragHandle
import ru.nn.tripnn.ui.common.InfiniteCarousel
import ru.nn.tripnn.ui.common.InternetProblemScreen
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.NewRouteButton
import ru.nn.tripnn.ui.common.RouteInfoBottomSheetContent
import ru.nn.tripnn.ui.common.TwoButtonBottomSheetDialog
import ru.nn.tripnn.ui.common.card.CARD_WIDTH
import ru.nn.tripnn.ui.common.card.LoadingCard
import ru.nn.tripnn.ui.common.card.RouteCard
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.screen.main.search.SearchPlaceBottomSheet
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun HomeScreen(
    recommendedRoutes: ResourceState<List<Route>>,
    currentRoute: ResourceState<CurrentRoute>,
    onAccountClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAllRoutesClick: () -> Unit,
    onNewRouteClick: () -> Unit,
    onCurrentRouteClick: () -> Unit,
    onTakeTheRoute: (Route) -> Unit,
    removeRouteFromFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    toPhotos: (String, Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            Menu(
                onAccountClick = onAccountClick,
                onHistoryClick = onHistoryClick,
                onFavouriteClick = onFavouriteClick,
                onSettingsClick = onSettingsClick,
                onClose = { scope.launch { drawerState.close() } }
            )
        },
        drawerState = drawerState
    ) {
        HomeContent(
            currentRoute = currentRoute,
            recommendedRoutes = recommendedRoutes,
            onCurrentRouteClick = onCurrentRouteClick,
            onAllRoutesClick = onAllRoutesClick,
            onNewRouteClick = onNewRouteClick,
            onMenuClick = {
                scope.launch {
                    drawerState.open()
                }
            },
            onTakeTheRoute = onTakeTheRoute,
            removeRouteFromFavourite = removeRouteFromFavourite,
            addRouteToFavourite = addRouteToFavourite,
            removePlaceFromFavourite = removePlaceFromFavourite,
            addPlaceToFavourite = addPlaceToFavourite,
            toPhotos = toPhotos
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    recommendedRoutes: ResourceState<List<Route>>,
    currentRoute: ResourceState<CurrentRoute>,
    onAllRoutesClick: () -> Unit,
    onNewRouteClick: () -> Unit,
    onCurrentRouteClick: () -> Unit,
    onMenuClick: () -> Unit,
    onTakeTheRoute: (Route) -> Unit,
    removeRouteFromFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    toPhotos: (String, Int) -> Unit
) {
    var showSearch by rememberSaveable { mutableStateOf(false) }
    var showRouteInfo by rememberSaveable { mutableStateOf(false) }
    var pickedRoute by rememberSaveable { mutableIntStateOf(0) }
    var showDeleteCurrentRouteDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(onMenuClick = onMenuClick)
        },
        containerColor = TripNnTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { paddings ->

        if (recommendedRoutes.isError || currentRoute.isError) {
            InternetProblemScreen()
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(paddings)
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TripNnTheme.colorScheme.secondaryBackground)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MontsText(
                        text = stringResource(id = R.string.recommended_routes),
                        style = MaterialTheme.typography.displayMedium
                    )
                    MontsText(
                        modifier = Modifier.clickable(onClick = onAllRoutesClick),
                        text = stringResource(id = R.string.all_txt),
                        style = MaterialTheme.typography.displayMedium,
                        color = TripNnTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (recommendedRoutes.isLoading) {
                    LoadingRecommendedRoutes()
                } else {
                    RecommendedRoutes(
                        routes = recommendedRoutes.value ?: listOf(),
                        onRouteClick = {
                            pickedRoute = it
                            showRouteInfo = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                AllPlacesButton(onClick = { showSearch = true })

                Spacer(modifier = Modifier.height(30.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                NewRouteButton(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        if (currentRoute.value?.buildInProgress == false) {
                            showDeleteCurrentRouteDialog = true
                        } else {
                            onNewRouteClick()
                        }
                    }
                )

                androidx.compose.animation.AnimatedVisibility(
                    visible = currentRoute.value != null && !currentRoute.value.buildInProgress,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut(),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    CurrentRouteBar(
                        route = currentRoute.value,
                        onClick = onCurrentRouteClick
                    )
                }
            }
        }

        if (showSearch) {
            SearchPlaceBottomSheet(onDismissRequest = { showSearch = false }, toPhotos = toPhotos)
        }

        if (showRouteInfo && recommendedRoutes.value != null) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { showRouteInfo = false },
                dragHandle = { DragHandle() },
                sheetState = sheetState,
                containerColor = TripNnTheme.colorScheme.bottomSheetBackground,
                windowInsets = WindowInsets(0)
            ) {
                RouteInfoBottomSheetContent(
                    removeRouteFromFavourite = { removeRouteFromFavourite(recommendedRoutes.value[pickedRoute].id) },
                    addRouteToFavourite = { addRouteToFavourite(recommendedRoutes.value[pickedRoute].id) },
                    removePlaceFromFavourite = removePlaceFromFavourite,
                    addPlaceToFavourite = addPlaceToFavourite,
                    route = recommendedRoutes.value[pickedRoute],
                    onTakeTheRoute = onTakeTheRoute,
                    toPhotos = toPhotos,
                    alreadyHasRoute = currentRoute.value != null
                )
            }
        }

        if (showDeleteCurrentRouteDialog) {
            TwoButtonBottomSheetDialog(
                title = stringResource(id = R.string.delete_current_route_title),
                text = stringResource(id = R.string.delete_current_route_text),
                rightButtonText = stringResource(id = R.string.delete_current_route_right_button_text),
                onSubmit = onNewRouteClick,
                onClose = { showDeleteCurrentRouteDialog = false }
            )
        }
    }
}

@Composable
fun TopAppBar(onMenuClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(TripNnTheme.colorScheme.secondaryBackground)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Icon(
                modifier = Modifier
                    .clickable(onClick = onMenuClick),
                painter = painterResource(id = R.drawable.burger_menu),
                contentDescription = stringResource(id = R.string.menu_txt),
                tint = TripNnTheme.colorScheme.tertiary
            )
        }
        Icon(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.tripnn_logo),
            contentDescription = stringResource(id = R.string.logo_txt),
            tint = TripNnTheme.colorScheme.textColor
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun Menu(
    onAccountClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.8f)
            .background(TripNnTheme.colorScheme.bottomSheetBackground)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Column {
            IconButton(
                onClick = onClose,
                modifier = Modifier.offset(x = (-14).dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cross_menu),
                    contentDescription = stringResource(id = R.string.close_menu),
                    modifier = Modifier.size(16.dp),
                    tint = TripNnTheme.colorScheme.tertiary
                )
            }

            MenuOption(onClick = onAccountClick, text = stringResource(id = R.string.account_txt))
            MenuOption(
                onClick = onHistoryClick,
                text = stringResource(id = R.string.routes_history)
            )
            MenuOption(onClick = onFavouriteClick, text = stringResource(id = R.string.favourites))
            MenuOption(onClick = onSettingsClick, text = stringResource(id = R.string.settings))
        }
    }
}

@Composable
fun MenuOption(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .height(80.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        MontsText(
            modifier = Modifier,
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecommendedRoutes(routes: List<Route>, onRouteClick: (Int) -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    InfiniteCarousel(
        modifier = Modifier.requiredWidth(screenWidth),
        pageSpacing = 11.dp,
        pageSize = PageSize.Fixed(CARD_WIDTH),
        contentPadding = PaddingValues(horizontal = screenWidth / 2 - CARD_WIDTH / 2),
        key = { routes[it].id },
        count = routes.size
    ) {
        RouteCard(
            route = routes[it],
            onCardClick = { onRouteClick(it) },
            modifier = Modifier.width(CARD_WIDTH)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoadingRecommendedRoutes() {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Box(modifier = Modifier.requiredWidth(screenWidth)) {
        InfiniteCarousel(
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 11.dp,
            pageSize = PageSize.Fixed(CARD_WIDTH),
            contentPadding = PaddingValues(horizontal = screenWidth / 2 - CARD_WIDTH / 2),
            key = { it },
            count = 3
        ) {
            LoadingCard()
        }
        Box(modifier = Modifier
            .matchParentSize()
            .pointerInput(false) {})
    }
}

@Composable
fun AllPlacesButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .shadow(borderRadius = 6.dp, blurRadius = 10.dp, spread = (-5).dp)
            .clip(RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .height(50.dp)
            .clickable(onClick = onClick)
            .background(TripNnTheme.colorScheme.cardBackground)
            .padding(vertical = 10.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.list_icon),
                contentDescription = stringResource(id = R.string.all_places),
                tint = TripNnTheme.colorScheme.tertiary
            )
            MontsText(
                text = stringResource(id = R.string.all_places_nn_txt),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    TripNNTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                HomeScreen(
                    currentRoute = ResourceState(
                        CurrentRoute(
                            places = ROUTE_1.places,
                            currentPlaceIndex = 2
                        )
                    ),
                    recommendedRoutes = ResourceState(ROUTES),
                    onAllRoutesClick = {},
                    onCurrentRouteClick = {},
                    onNewRouteClick = {},
                    onAccountClick = {},
                    onFavouriteClick = {},
                    onSettingsClick = {},
                    onHistoryClick = {},
                    addPlaceToFavourite = {},
                    addRouteToFavourite = {},
                    removePlaceFromFavourite = {},
                    removeRouteFromFavourite = {},
                    toPhotos = { _, _ -> },
                    onTakeTheRoute = {}
                )
            }
        }
    }
}