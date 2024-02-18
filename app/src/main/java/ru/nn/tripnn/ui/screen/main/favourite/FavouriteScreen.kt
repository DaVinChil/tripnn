package ru.nn.tripnn.ui.screen.main.favourite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.data.stub_data.PLACE_FULL_1
import ru.nn.tripnn.data.stub_data.ROUTES
import ru.nn.tripnn.data.stub_data.ROUTE_FULL
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.PlaceFull
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.entity.RouteFull
import ru.nn.tripnn.ui.common.AddToFavouriteCardOption
import ru.nn.tripnn.ui.common.CatalogNavigation
import ru.nn.tripnn.ui.common.DraggableCard
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PlaceCard
import ru.nn.tripnn.ui.common.RemoveFromFavouriteGoldCardOption
import ru.nn.tripnn.ui.common.RemoveFromFavouriteRedCardOption
import ru.nn.tripnn.ui.common.RouteCard
import ru.nn.tripnn.ui.common.Search
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
    isLoading: Boolean,
    favouritePlaces: List<Place>,
    favouriteRoutes: List<Route>,
    placeFull: PlaceFull,
    routeFull: RouteFull,
    getPlaceFullInfo: (String) -> Unit,
    getRouteFullInfo: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    removeRouteFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit
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
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }

        MontsText(text = "Избранные", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(10.dp))

        CatalogNavigation(
            catalogs = listOf("Места", "Маршруты"),
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
                chosen = PLACES_INDEX
                FavouritePlacesContent(
                    places = favouritePlaces,
                    placeFull = placeFull,
                    getPlaceFullInfo = getPlaceFullInfo,
                    removeFromFavourite = removePlaceFromFavourite,
                    addToFavourite = addPlaceToFavourite
                )
            }
            composable(route = DESTINATION[ROUTES_INDEX]) {
                chosen = ROUTES_INDEX
                FavouriteRoutesContent(
                    routes = favouriteRoutes,
                    routeFull = routeFull,
                    placeFull = placeFull,
                    getRouteFullInfo = getRouteFullInfo,
                    getPlaceFullInfo = getPlaceFullInfo,
                    removeRouteFromFavourite = removeRouteFromFavourite,
                    addRouteToFavourite = addRouteToFavourite,
                    removePlaceFromFavourite = removePlaceFromFavourite,
                    addPlaceToFavourite = addPlaceToFavourite
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritePlacesContent(
    places: List<Place>,
    placeFull: PlaceFull,
    getPlaceFullInfo: (String) -> Unit,
    removeFromFavourite: (String) -> Unit,
    addToFavourite: (String) -> Unit
) {
    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardInfo by remember { mutableStateOf(false) }

    LazyColumn(
        state = lazyState,
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(items = places, key = Place::id) { place ->
            val option: @Composable () -> Unit =
                @Composable {
                    RemoveFromFavouriteRedCardOption(
                        onClick = { removeFromFavourite(place.id) })
                }
            DraggableCard(option1 = option) {
                PlaceCard(
                    place = place,
                    onCardClick = {
                        getPlaceFullInfo(place.id)
                        showCardInfo = true
                    },
                    shadowColor = Color.Black.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showCardInfo) {
        val cor = rememberCoroutineScope()
        ModalBottomSheet(
            onDismissRequest = { showCardInfo = false },
            dragHandle = null,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            PlaceInfoBottomSheet(
                placeFull = placeFull,
                onClose = {
                    cor.launch {
                        sheetState.hide()
                    }.invokeOnCompletion { showCardInfo = false }
                },
                removeFromFavourite = { removeFromFavourite(placeFull.id) },
                addToFavourite = { addToFavourite(placeFull.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteRoutesContent(
    routes: List<Route>,
    routeFull: RouteFull,
    placeFull: PlaceFull,
    getRouteFullInfo: (String) -> Unit,
    getPlaceFullInfo: (String) -> Unit,
    removeRouteFromFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit
) {
    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState()
    var showRouteInfo by remember { mutableStateOf(false) }

    LazyColumn(
        state = lazyState,
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(items = routes, key = Route::id) { route ->
            val option: @Composable () -> Unit =
                @Composable {
                    RemoveFromFavouriteRedCardOption(
                        onClick = { removeRouteFromFavourite(route.id) })
                }
            DraggableCard(option1 = option) {
                RouteCard(
                    route = route,
                    onCardClick = {
                        getRouteFullInfo(route.id)
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
            containerColor = MaterialTheme.colorScheme.background
        ) {
            RouteInfoBottomSheet(
                routeFull = routeFull,
                placeFull = placeFull,
                removeRouteFromFavourite = { removeRouteFromFavourite(routeFull.id) },
                addRouteToFavourite = { addRouteToFavourite(routeFull.id) },
                removePlaceFromFavourite = removePlaceFromFavourite,
                addPlaceToFavourite = addPlaceToFavourite,
                getPlaceFullInfo = getPlaceFullInfo
            )
        }
    }


}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RouteInfoBottomSheet(
    routeFull: RouteFull,
    placeFull: PlaceFull,
    removeRouteFromFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    getPlaceFullInfo: (String) -> Unit
) {
    var favourite by remember { mutableStateOf(routeFull.favourite) }
    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardInfo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(5f / 6f)
            .padding(horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MontsText(
                text = routeFull.name, fontSize = 20.sp, fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth(4f / 6f)
                    .basicMarquee()
            )
            Spacer(modifier = Modifier.width(5.dp))

            if (routeFull.rating != null) {
                Icon(
                    painter = painterResource(id = R.drawable.gold_small_start),
                    contentDescription = "gold star",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(5.dp))
                MontsText(
                    text = routeFull.rating.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1DAB4D),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier.size(20.dp).clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    onClick = {
                        favourite = if (favourite) {
                            removeRouteFromFavourite(routeFull.id)
                            false
                        } else {
                            addRouteToFavourite(routeFull.id)
                            true
                        }
                    }
                ),
                painter = painterResource(id = if (favourite) R.drawable.gold_bookmark else R.drawable.gray_bookmark),
                contentDescription = "bookmark",
                tint = Color.Unspecified
            )
        }

        LazyColumn(
            state = lazyState,
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(items = routeFull.places, key = Place::id) { place ->
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
                        onCardClick = { showCardInfo = true; getPlaceFullInfo(place.id) },
                        shadowColor = Color.Black.copy(alpha = 0.2f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (showCardInfo) {
        val cor = rememberCoroutineScope()
        ModalBottomSheet(
            onDismissRequest = { showCardInfo = false },
            dragHandle = null,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            PlaceInfoBottomSheet(
                placeFull = placeFull,
                onClose = {
                    cor.launch {
                        sheetState.hide()
                    }.invokeOnCompletion { showCardInfo = false }
                },
                removeFromFavourite = { removePlaceFromFavourite(placeFull.id) },
                addToFavourite = { addPlaceToFavourite(placeFull.id) }
            )
        }
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
            isLoading = false,
            favouritePlaces = listOf(PLACE_1),
            favouriteRoutes = ROUTES,
            placeFull = PLACE_FULL_1,
            routeFull = ROUTE_FULL,
            getPlaceFullInfo = { /*TODO*/ },
            getRouteFullInfo = { /*TODO*/ },
            removePlaceFromFavourite = { /*TODO*/ },
            removeRouteFromFavourite = { /*TODO*/ },
            addPlaceToFavourite = { /*TODO*/ },
            addRouteToFavourite = { /*TODO*/ }
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
            RouteInfoBottomSheet(
                routeFull = ROUTE_FULL,
                removeRouteFromFavourite = { },
                addRouteToFavourite = { },
                removePlaceFromFavourite = {},
                addPlaceToFavourite = {},
                placeFull = PLACE_FULL_1,
                getPlaceFullInfo = {}
            )
        }
    }
}