package ru.nn.tripnn.ui.screen.main.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.common.CatalogNavigation
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.Search
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.screen.main.favourite.CATALOGS
import ru.nn.tripnn.ui.screen.main.favourite.PLACES_INDEX
import ru.nn.tripnn.ui.common.PlacesColumn
import ru.nn.tripnn.ui.screen.main.favourite.ROUTES_INDEX
import ru.nn.tripnn.ui.common.RoutesColumn
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    filterPlaces: (String) -> Unit,
    filterRoutes: (String) -> Unit,
    places: ResourceState<List<Place>>,
    routes: ResourceState<List<Route>>,
    removePlaceFromFavourite: (String) -> Unit,
    removeRouteFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
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
            text = stringResource(id = R.string.history),
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
                    places = places,
                    removeFromFavourite = removePlaceFromFavourite,
                    addToFavourite = addPlaceToFavourite,
                    onEmpty = { HistoryEmptyResult() },
                    toPhotos = toPhotos
                )
            }
            composable(route = CATALOGS[ROUTES_INDEX]) {
                RoutesColumn(
                    routes = routes,
                    removeRouteFromFavourite = removeRouteFromFavourite,
                    addRouteToFavourite = addRouteToFavourite,
                    removePlaceFromFavourite = removePlaceFromFavourite,
                    addPlaceToFavourite = addPlaceToFavourite,
                    onTakeTheRoute = onTakeTheRoute,
                    onEmpty = { HistoryEmptyResult() },
                    toPhotos = toPhotos,
                    alreadyHasRoute = alreadyHasRoute
                )
            }
        }
    }
}

@Composable
fun HistoryEmptyResult() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(1 / 3f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MontsText(
                text = stringResource(id = R.string.empty),
                fontSize = 18.sp,
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(35.dp))
            MontsText(text = "☹\uFE0F", fontSize = 50.sp)
            Spacer(modifier = Modifier.height(35.dp))
            MontsText(
                text = stringResource(id = R.string.empty_history_comment),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}