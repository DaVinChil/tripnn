package ru.nn.tripnn.ui.screen.main.favourite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.common.CatalogNavigation
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PlacesColumn
import ru.nn.tripnn.ui.common.RoutesColumn
import ru.nn.tripnn.ui.common.Search
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

val CATALOGS = listOf("places", "routes")
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
    removeRouteFromFavourite: (Route) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    addRouteToFavourite: (Route) -> Unit,
    onTakeTheRoute: (Route) -> Unit,
    toPhotos: (String, Int) -> Unit,
    alreadyHasRoute: Boolean
) {
    val navController = rememberNavController()

    var chosen by remember { mutableIntStateOf(0) }
    var word by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TripNnTheme.colorScheme.background)
            .padding(10.dp)
            .systemBarsPadding()
    ) {

        IconButton(onClick = onBack, modifier = Modifier.offset(x = (-16).dp)) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = stringResource(id = R.string.back_txt),
                tint = TripNnTheme.colorScheme.tertiary
            )
        }

        MontsText(
            text = stringResource(id = R.string.favourites),
            style = MaterialTheme.typography.titleMedium,
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
                navController.navigate(CATALOGS[it]) {
                    launchSingleTop = true
                    popUpTo(CATALOGS[it]) {
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

        NavHost(navController = navController, startDestination = CATALOGS[PLACES_INDEX]) {
            composable(route = CATALOGS[PLACES_INDEX]) {
                PlacesColumn(
                    places = favouritePlaces,
                    removeFromFavourite = removePlaceFromFavourite,
                    addToFavourite = addPlaceToFavourite,
                    onEmpty = { FavouriteEmptyResult() },
                    toPhotos = toPhotos
                )
            }
            composable(route = CATALOGS[ROUTES_INDEX]) {
                RoutesColumn(
                    routes = favouriteRoutes,
                    removeRouteFromFavourite = removeRouteFromFavourite,
                    addRouteToFavourite = addRouteToFavourite,
                    removePlaceFromFavourite = removePlaceFromFavourite,
                    addPlaceToFavourite = addPlaceToFavourite,
                    onTakeTheRoute = onTakeTheRoute,
                    onEmpty = { FavouriteEmptyResult() },
                    toPhotos = toPhotos,
                    alreadyHasRoute = alreadyHasRoute
                )
            }
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
            text = stringResource(id = R.string.empty),
            style = MaterialTheme.typography.displayLarge
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
                    style = MaterialTheme.typography.labelLarge
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
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )

            MontsText(
                text = stringResource(id = R.string.empty_favourite_comment_3),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
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
            favouritePlaces = ResourceState(value = listOf(PLACE_1)),
            favouriteRoutes = ResourceState(value = ROUTES),
            removePlaceFromFavourite = { /*TODO*/ },
            removeRouteFromFavourite = { /*TODO*/ },
            addPlaceToFavourite = { /*TODO*/ },
            addRouteToFavourite = { /*TODO*/ },
            onTakeTheRoute = {},
            toPhotos = { _, _ -> },
            alreadyHasRoute = false
        )
    }
}

