package ru.nn.tripnn.ui.screen.main.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.ROUTES
import ru.nn.tripnn.data.stub_data.ROUTE_1
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.screen.HomeScreenData
import ru.nn.tripnn.ui.common.CARD_WIDTH
import ru.nn.tripnn.ui.common.DragHandle
import ru.nn.tripnn.ui.common.LoadingCard
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.RouteCard
import ru.nn.tripnn.ui.common.lightShimmer
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.screen.main.favourite.RouteInfoBottomSheetContent
import ru.nn.tripnn.ui.screen.main.search.SearchPlaceBottomSheet
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.montserratFamily
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(
    homeScreenState: ResourceState<HomeScreenData>,
    onAccountClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAllRoutesClick: () -> Unit,
    onNewRouteClick: () -> Unit,
    onCurRouteClick: (() -> Unit)? = null,
    removeRouteFromFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
) {
    val screenData = homeScreenState.value

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
            curRoutePercent = screenData?.curRoutePercent,
            recRoutes = screenData?.recommendedRoutes ?: listOf(),
            onCurRouteClick = onCurRouteClick,
            onAllRoutesClick = onAllRoutesClick,
            onNewRouteClick = onNewRouteClick,
            onMenuClick = {
                scope.launch {
                    drawerState.open()
                }
            },
            removeRouteFromFavourite = removeRouteFromFavourite,
            addRouteToFavourite = addRouteToFavourite,
            removePlaceFromFavourite = removePlaceFromFavourite,
            addPlaceToFavourite = addPlaceToFavourite,
            isLoading = homeScreenState.isLoading,
            isError = homeScreenState.isError
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    isLoading: Boolean,
    isError: Boolean,
    recRoutes: List<Route>,
    onAllRoutesClick: () -> Unit,
    onNewRouteClick: () -> Unit,
    curRoutePercent: Int?,
    onCurRouteClick: (() -> Unit)? = null,
    onMenuClick: () -> Unit,
    removeRouteFromFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(onMenuClick = onMenuClick)
        }
    ) { paddings ->
        var showSearch by remember { mutableStateOf(false) }
        var showRouteInfo by remember { mutableStateOf(false) }
        var pickedRoute by remember { mutableStateOf(ROUTE_1) }

        if (isError) {
            InternetProblem()
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(paddings)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onSurface)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MontsText(
                        text = stringResource(id = R.string.recommended_routes),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    MontsText(
                        modifier = Modifier.clickable(onClick = onAllRoutesClick),
                        text = stringResource(id = R.string.all_txt),
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isLoading) {
                    LoadingRecommendedRoutes()
                } else {
                    RecommendedRoutes(
                        routes = recRoutes,
                        onRouteClick = {
                            pickedRoute = recRoutes[it]
                            showRouteInfo = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                AllPlacesButton(onClick = { showSearch = true })

                Spacer(modifier = Modifier.height(30.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    NewRouteButton(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = onNewRouteClick
                    )
                }
                if (onCurRouteClick != null && curRoutePercent != null) {
                    CurrentRouteBar(
                        percent = curRoutePercent,
                        onClick = onCurRouteClick
                    )
                } else if (isLoading) {
                    LoadingCurrentRouteBar()
                }
            }
        }

        if (showSearch) {
            SearchPlaceBottomSheet(onDismissRequest = { showSearch = false })
        }

        if (showRouteInfo) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { showRouteInfo = false },
                dragHandle = { DragHandle() },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.background,
                windowInsets = WindowInsets(0)
            ) {
                RouteInfoBottomSheetContent(
                    removeRouteFromFavourite = { removeRouteFromFavourite(pickedRoute.id) },
                    addRouteToFavourite = { addRouteToFavourite(pickedRoute.id) },
                    removePlaceFromFavourite = removePlaceFromFavourite,
                    addPlaceToFavourite = addPlaceToFavourite,
                    route = pickedRoute,
                    onTakeTheRoute = { TODO() }
                )
            }
        }
    }
}

@Composable
fun InternetProblem() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MontsText(text = "Internet problems", fontSize = 20.sp)
    }
}

@Composable
fun TopAppBar(onMenuClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSurface)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Icon(
                modifier = Modifier
                    .clickable(onClick = onMenuClick),
                painter = painterResource(id = R.drawable.burger_menu),
                contentDescription = stringResource(id = R.string.menu_txt),
            )
        }
        Image(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.tripnn_logo),
            contentDescription = stringResource(id = R.string.logo_txt)
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
            .background(MaterialTheme.colorScheme.background)
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
                    modifier = Modifier.size(16.dp)
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
            fontSize = 18.sp,
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
            shadowColor = Color.Black.copy(alpha = 0.2f),
            modifier = Modifier
                .width(CARD_WIDTH)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfiniteCarousel(
    modifier: Modifier = Modifier,
    count: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSize: PageSize = PageSize.Fill,
    pageSpacing: Dp = 0.dp,
    key: (index: Int) -> Any,
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    if (count == 0) return

    val state = rememberPagerState(pageCount = { Int.MAX_VALUE }, initialPage = 500)
    val flingBehavior = PagerDefaults.flingBehavior(
        state = state,
        pagerSnapDistance = PagerSnapDistance.atMost(10),
    )
    val scaleFactor = 3f

    HorizontalPager(
        modifier = modifier,
        state = state,
        pageSpacing = pageSpacing,
        pageSize = pageSize,
        contentPadding = contentPadding,
        flingBehavior = flingBehavior,
        key = { key(it % count) }
    ) { index ->
        val scale = if (state.currentPage == index) {
            1 - state.currentPageOffsetFraction.absoluteValue / scaleFactor
        } else if ((state.currentPage - index).absoluteValue == 1 &&
            (state.currentPage < index && state.currentPageOffsetFraction > 0 ||
                    state.currentPage > index && state.currentPageOffsetFraction < 0)
        ) {
            (scaleFactor - 1) / scaleFactor + state.currentPageOffsetFraction.absoluteValue / scaleFactor
        } else {
            (scaleFactor - 1) / scaleFactor
        }

        val offset = (CARD_WIDTH - CARD_WIDTH * scale) / 2

        Box(
            modifier = Modifier
                .offset(
                    x = offset * if (state.currentPage < index) -1
                    else if (state.currentPage > index) 1
                    else if (state.currentPageOffsetFraction <= 0) -1
                    else 1
                )
                .scale(scale = scale)
        ) {
            pageContent(index % count)
        }
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
            .background(Color.White)
            .padding(vertical = 10.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.list_icon),
                contentDescription = stringResource(id = R.string.all_places)
            )
            MontsText(
                text = stringResource(id = R.string.all_places_nn_txt),
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
fun NewRouteButton(modifier: Modifier = Modifier, onClick: () -> Unit) {

    val buttonInteractionSource = remember { MutableInteractionSource() }
    val pressed by buttonInteractionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (pressed) 0.85f else 1f, label = "")

    val height = 140.dp
    val nrTextSize = 40.sp
    val createTextSize = 12.sp
    val lineHeight = 30.sp
    val yOffset = (-9).dp

    Box(
        modifier = modifier
            .scale(scale)
            .height(height)
            .width(230.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = buttonInteractionSource
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(height)
                .shadow(
                    borderRadius = 100.dp,
                    blurRadius = 20.dp,
                    color = Color.Black.copy(alpha = 0.3f)
                )
                .clip(RoundedCornerShape(100))
                .background(MaterialTheme.colorScheme.onSurface)
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.new_route),
                fontFamily = montserratFamily,
                fontSize = nrTextSize,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center,
                lineHeight = lineHeight,
                letterSpacing = (-0.5).sp
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopCenter) {
                MontsText(
                    text = stringResource(id = R.string.create),
                    fontSize = createTextSize,
                    modifier = Modifier.offset(y = yOffset)
                )
            }
        }
    }
}

@Composable
fun LoadingCurrentRouteBar() {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View, theme = lightShimmer)

    Box(
        modifier = Modifier
            .shadow(
                borderRadius = 6.dp,
                blurRadius = 10.dp,
                spread = (-5).dp,
                color = Color.Black.copy(alpha = 0.3f)
            )
            .clip(RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .height(60.dp)
            .shimmer(shimmer)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.tertiary)
        )
    }
}

@Composable
fun CurrentRouteBar(
    modifier: Modifier = Modifier,
    percent: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
            .shadow(
                borderRadius = 6.dp,
                blurRadius = 10.dp,
                spread = (-5).dp,
                color = Color.Black.copy(alpha = 0.3f)
            )
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(vertical = 10.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            MontsText(
                text = stringResource(id = R.string.current_route),
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                MontsText(text = "$percent%", fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.map_icon),
                    contentDescription = stringResource(id = R.string.map_desc_icon),
                    tint = Color.White
                )
            }

        }
    }
}

@Preview
@Composable
fun CurrentRouteBarPreview() {
    TripNNTheme {
        CurrentRouteBar(percent = 13, onClick = {})
    }
}

@Preview
@Composable
fun NewRouteButtonPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp)
        ) {
            NewRouteButton(onClick = {})
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
                    homeScreenState = ResourceState(
                        value = HomeScreenData(
                            recommendedRoutes = ROUTES,
                            curRoutePercent = 33
                        )
                    ),
                    onAllRoutesClick = {},
                    onCurRouteClick = {},
                    onNewRouteClick = {},
                    onAccountClick = {},
                    onFavouriteClick = {},
                    onSettingsClick = {},
                    onHistoryClick = {},
                    addPlaceToFavourite = {},
                    addRouteToFavourite = {},
                    removePlaceFromFavourite = {},
                    removeRouteFromFavourite = {}
                )
            }
        }
    }
}