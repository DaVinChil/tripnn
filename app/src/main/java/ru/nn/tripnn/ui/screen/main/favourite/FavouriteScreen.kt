package ru.nn.tripnn.ui.screen.main.favourite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.data.stub_data.ROUTES
import ru.nn.tripnn.data.stub_data.ROUTE_1
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.ui.common.AddToFavouriteCardOption
import ru.nn.tripnn.ui.common.CatalogNavigation
import ru.nn.tripnn.ui.common.DragHandle
import ru.nn.tripnn.ui.common.DraggableCard
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PlaceCard
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.common.Rating
import ru.nn.tripnn.ui.common.RemoveFromFavouriteGoldCardOption
import ru.nn.tripnn.ui.common.RemoveFromFavouriteRedCardOption
import ru.nn.tripnn.ui.common.RouteCard
import ru.nn.tripnn.ui.common.Search
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.screen.main.home.InternetProblem
import ru.nn.tripnn.ui.screen.main.search.PlaceInfoBottomSheet
import ru.nn.tripnn.ui.theme.TripNNTheme

val DESTINATION = listOf("places", "routes")
const val PLACES_INDEX = 0
const val ROUTES_INDEX = 1

@Composable
fun FavouriteScreen(
    onBack: () -> Unit,
    filterPlaces: (String) -> Unit,
    filterRoutes: (String) -> Unit,
    favouritePlaces: ResourceState<List<Place>>,
    favouriteRoutes: ResourceState<List<Route>>,
    removePlaceFromFavourite: (String) -> Unit,
    removeRouteFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    onTakeTheRoute: (String) -> Unit
) {
    val navController = rememberNavController()

    var chosen by remember { mutableIntStateOf(0) }
    var word by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
            .systemBarsPadding()
    ) {

        IconButton(onClick = onBack, modifier = Modifier.offset(x = (-16).dp)) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = stringResource(id = R.string.back_txt),
                tint = MaterialTheme.colorScheme.tertiary
            )
        }

        MontsText(
            text = stringResource(id = R.string.favourites),
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        CatalogNavigation(
            catalogs = listOf(
                stringResource(id = R.string.places),
                stringResource(id = R.string.routes)
            ),
            onCatalogChange = {
                if (it == 0) {
                    filterPlaces(word)
                } else {
                    filterRoutes(word)
                }
                navController.navigate(DESTINATION[it]) {
                    launchSingleTop = true
                    popUpTo(DESTINATION[it]) {
                        inclusive = true
                    }
                }
                chosen = it
            },
            chosen = chosen,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Search(
            onSearch = {
                word = it
                if (chosen == 0) {
                    filterPlaces(word)
                } else {
                    filterRoutes(word)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        NavHost(navController = navController, startDestination = DESTINATION[PLACES_INDEX]) {
            composable(route = DESTINATION[PLACES_INDEX]) {
                PlacesColumn(
                    places = favouritePlaces,
                    removeFromFavourite = removePlaceFromFavourite,
                    addToFavourite = addPlaceToFavourite,
                    onEmpty = { FavouriteEmptyResult() }
                )
            }
            composable(route = DESTINATION[ROUTES_INDEX]) {
                RoutesColumn(
                    routes = favouriteRoutes,
                    removeRouteFromFavourite = removeRouteFromFavourite,
                    addRouteToFavourite = addRouteToFavourite,
                    removePlaceFromFavourite = removePlaceFromFavourite,
                    addPlaceToFavourite = addPlaceToFavourite,
                    onTakeTheRoute = onTakeTheRoute,
                    onEmpty = { FavouriteEmptyResult() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesColumn(
    onEmpty: @Composable () -> Unit,
    places: ResourceState<List<Place>>,
    removeFromFavourite: (String) -> Unit,
    addToFavourite: (String) -> Unit
) {
    if (places.isError) {
        InternetProblem()
        return
    }

    if (places.isLoading) {
        LoadingCircleScreen()
        return
    }

    if (places.value.isNullOrEmpty()) {
        onEmpty()
        return
    }

    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardInfo by remember { mutableStateOf(false) }
    var pickedPlace by remember { mutableStateOf(PLACE_1) }

    LazyColumn(
        state = lazyState,
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(items = places.value, key = Place::id) { place ->
            val option: @Composable () -> Unit =
                @Composable {
                    RemoveFromFavouriteRedCardOption(
                        onClick = { removeFromFavourite(place.id) })
                }
            DraggableCard(option1 = option) {
                PlaceCard(
                    place = place,
                    onCardClick = {
                        pickedPlace = place
                        showCardInfo = true
                    },
                    shadowColor = Color.Black.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showCardInfo) {
        PlaceInfoBottomSheet(
            place = pickedPlace,
            onDismissRequest = { showCardInfo = false },
            sheetState = sheetState,
            removeFromFavourite = { removeFromFavourite(pickedPlace.id) },
            addToFavourite = { addToFavourite(pickedPlace.id) }
        )
    }
}

@Composable
fun LoadingCircleScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesColumn(
    routes: ResourceState<List<Route>>,
    onEmpty: @Composable () -> Unit,
    removeRouteFromFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    onTakeTheRoute: (String) -> Unit
) {
    if (routes.isError) {
        InternetProblem()
        return
    }

    if (routes.isLoading) {
        LoadingCircleScreen()
        return
    }

    if (routes.value.isNullOrEmpty()) {
        onEmpty()
        return
    }

    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showRouteInfo by remember { mutableStateOf(false) }
    var pickedRoute by remember { mutableStateOf(ROUTE_1) }

    LazyColumn(
        state = lazyState,
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(items = routes.value, key = Route::id) { route ->
            val option: @Composable () -> Unit =
                @Composable {
                    RemoveFromFavouriteRedCardOption(
                        onClick = { removeRouteFromFavourite(route.id) })
                }
            DraggableCard(option1 = option) {
                RouteCard(
                    route = route,
                    onCardClick = {
                        pickedRoute = route
                        showRouteInfo = true
                    },
                    shadowColor = Color.Black.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showRouteInfo) {
        ModalBottomSheet(
            onDismissRequest = { showRouteInfo = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            dragHandle = { DragHandle() }
        ) {
            RouteInfoBottomSheetContent(
                route = pickedRoute,
                removeRouteFromFavourite = { removeRouteFromFavourite(pickedRoute.id) },
                addRouteToFavourite = { addRouteToFavourite(pickedRoute.id) },
                removePlaceFromFavourite = removePlaceFromFavourite,
                addPlaceToFavourite = addPlaceToFavourite,
                onTakeTheRoute = onTakeTheRoute
            )
        }
    }
}

@Composable
fun FavouriteEmptyResult() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MontsText(
            text = stringResource(id = R.string.empty), fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(35.dp))
        MontsText(text = "☹\uFE0F", fontSize = 50.sp)
        Spacer(modifier = Modifier.height(35.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                MontsText(
                    text = stringResource(id = R.string.empty_favourite_comment_1),
                    fontSize = 16.sp
                )
                Icon(
                    painter = painterResource(id = R.drawable.unselected_bookmark),
                    contentDescription = stringResource(
                        id = R.string.favourite
                    )
                )
            }

            MontsText(
                text = stringResource(id = R.string.empty_favourite_comment_2),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            MontsText(
                text = stringResource(id = R.string.empty_favourite_comment_3),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteInfoBottomSheetContent(
    route: Route,
    removeRouteFromFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    onTakeTheRoute: (String) -> Unit
) {
    var favourite by remember { mutableStateOf(route.favourite) }
    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardInfo by remember { mutableStateOf(false) }
    var pickedPlace by remember { mutableStateOf(route.places[0]) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(5f / 6f)
            .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                MontsText(
                    text = route.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(4f / 6f)
                )
                Spacer(modifier = Modifier.width(5.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (route.rating != null) {
                        Icon(
                            painter = painterResource(id = R.drawable.gold_small_start),
                            contentDescription = stringResource(id = R.string.gold_star),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Rating(route.rating, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                onClick = {
                                    favourite = if (favourite) {
                                        removeRouteFromFavourite(route.id)
                                        false
                                    } else {
                                        addRouteToFavourite(route.id)
                                        true
                                    }
                                }
                            ),
                        painter = painterResource(id = if (favourite) R.drawable.gold_bookmark else R.drawable.gray_bookmark),
                        contentDescription = stringResource(id = R.string.bookmark),
                        tint = Color.Unspecified
                    )
                }
            }

            if (route.desc != null) {
                Spacer(modifier = Modifier.height(10.dp))
                MontsText(
                    text = route.desc,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn(
                state = lazyState,
                contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items = route.places, key = Place::id) { place ->
                    val option: @Composable () -> Unit = if (place.favourite) {
                        @Composable {
                            RemoveFromFavouriteGoldCardOption(
                                onClick = { removePlaceFromFavourite(place.id) })
                        }
                    } else {
                        @Composable { AddToFavouriteCardOption(onClick = { addPlaceToFavourite(place.id) }) }
                    }
                    DraggableCard(option1 = option) {
                        PlaceCard(
                            place = place,
                            onCardClick = { showCardInfo = true; pickedPlace = place },
                            shadowColor = Color.Black.copy(alpha = 0.2f),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        PrimaryButton(
            text = stringResource(id = R.string.take_the_route),
            onClick = { onTakeTheRoute(route.id) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    if (showCardInfo) {
        PlaceInfoBottomSheet(
            place = pickedPlace,
            sheetState = sheetState,
            onDismissRequest = { showCardInfo = false },
            removeFromFavourite = { removePlaceFromFavourite(pickedPlace.id) },
            addToFavourite = { addPlaceToFavourite(pickedPlace.id) }
        )
    }
}

@Preview
@Composable
fun FavouriteScreenPreview() {
    TripNNTheme {
        FavouriteScreen(
            onBack = { /*TODO*/ },
            filterPlaces = { /*TODO*/ },
            filterRoutes = { /*TODO*/ },
            favouritePlaces = ResourceState(value = listOf(PLACE_1)),
            favouriteRoutes = ResourceState(value = ROUTES),
            removePlaceFromFavourite = { /*TODO*/ },
            removeRouteFromFavourite = { /*TODO*/ },
            addPlaceToFavourite = { /*TODO*/ },
            addRouteToFavourite = { /*TODO*/ },
            onTakeTheRoute = {}
        )
    }
}

@Preview
@Composable
fun RouteInfoPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            RouteInfoBottomSheetContent(
                route = ROUTE_1,
                removeRouteFromFavourite = { },
                addRouteToFavourite = { },
                removePlaceFromFavourite = {},
                addPlaceToFavourite = {},
                onTakeTheRoute = {}
            )
        }
    }
}