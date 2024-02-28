package ru.nn.tripnn.ui.screen.main.history

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.ui.common.CatalogNavigation
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.Search
import ru.nn.tripnn.ui.screen.main.favourite.DESTINATION
import ru.nn.tripnn.ui.screen.main.favourite.FavouritePlacesContent
import ru.nn.tripnn.ui.screen.main.favourite.PLACES_INDEX
import ru.nn.tripnn.ui.screen.main.favourite.ROUTES_INDEX
import ru.nn.tripnn.ui.screen.main.favourite.ResourceListState
import ru.nn.tripnn.ui.screen.main.favourite.RoutesContent

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    filterPlaces: (String) -> Unit,
    filterRoutes: (String) -> Unit,
    isLoading: Boolean,
    places: ResourceListState<Place>,
    routes: ResourceListState<Route>,
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

        MontsText(text = stringResource(id = R.string.history), fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(10.dp))

        CatalogNavigation(
            catalogs = listOf(stringResource(id = R.string.places), stringResource(id = R.string.routes)),
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
                    places = places.list,
                    removeFromFavourite = removePlaceFromFavourite,
                    addToFavourite = addPlaceToFavourite
                )
            }
            composable(route = DESTINATION[ROUTES_INDEX]) {
                chosen = ROUTES_INDEX
                RoutesContent(
                    routes = routes.list,
                    removeRouteFromFavourite = removeRouteFromFavourite,
                    addRouteToFavourite = addRouteToFavourite,
                    removePlaceFromFavourite = removePlaceFromFavourite,
                    addPlaceToFavourite = addPlaceToFavourite,
                    onTakeTheRoute = onTakeTheRoute
                )
            }
        }
    }
}