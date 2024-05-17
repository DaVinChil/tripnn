package ru.nn.tripnn.ui.screen.main.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import ru.nn.tripnn.data.datasource.stubdata.ui.PLACE_1
import ru.nn.tripnn.data.datasource.stubdata.ui.ROUTES
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.common.CatalogNavigation
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PlacesColumn
import ru.nn.tripnn.ui.common.RoutesColumn
import ru.nn.tripnn.ui.common.Search
import ru.nn.tripnn.ui.common.TwoButtonBottomSheetDialog
import ru.nn.tripnn.ui.common.card.RemoveFromHistoryOption
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.screen.main.favourite.CATALOGS
import ru.nn.tripnn.ui.screen.main.favourite.PLACES_INDEX
import ru.nn.tripnn.ui.screen.main.favourite.ROUTES_INDEX
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    filterByWord: (String) -> Unit,
    places: ResourceState<List<Place>>,
    routes: ResourceState<List<Route>>,
    removePlaceFromFavourite: (Place) -> Unit,
    removeRouteFromFavourite: (Route) -> Unit,
    removePlaceFromHistory: (Place) -> Unit,
    removeRouteFromHistory: (Route) -> Unit,
    clearRoutesHistory: () -> Unit,
    clearPlacesHistory: () -> Unit,
    addPlaceToFavourite: (Place) -> Unit,
    addRouteToFavourite: (Route) -> Unit,
    onTakeTheRoute: (Route) -> Unit,
    toPhotos: (String, Int) -> Unit,
    alreadyHasRoute: Boolean
) {
    val navController = rememberNavController()

    var chosen by remember { mutableIntStateOf(0) }
    var word by remember { mutableStateOf("") }

    var showClearHistoryDialog by remember { mutableStateOf(false) }
    val clearHistory by remember(chosen) {
        mutableStateOf(if (chosen == PLACES_INDEX) clearPlacesHistory else clearRoutesHistory)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TripNnTheme.colorScheme.background)
            .padding(10.dp)
            .systemBarsPadding()
    ) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = onBack, modifier = Modifier.offset(x = (-16).dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(id = R.string.back_txt),
                    tint = TripNnTheme.colorScheme.tertiary
                )
            }

            if ((chosen == PLACES_INDEX && !places.state.isNullOrEmpty() && places.isSuccessAndNotNull())
                || (chosen == ROUTES_INDEX && !routes.state.isNullOrEmpty() && routes.isSuccessAndNotNull())
            ) {
                IconButton(onClick = { showClearHistoryDialog = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.trashcan_small),
                        contentDescription = stringResource(id = R.string.delete),
                        tint = TripNnTheme.colorScheme.tertiary
                    )
                }
            }
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
                filterByWord(word)
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
                filterByWord(word)
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
                    toPhotos = toPhotos,
                    hideIndication = true,
                    option2 = { RemoveFromHistoryOption { removePlaceFromHistory(it) } }
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
                    alreadyHasRoute = alreadyHasRoute,
                    hideIndication = true,
                    option2 = { RemoveFromHistoryOption { removeRouteFromHistory(it) } }
                )
            }
        }
    }

    if (showClearHistoryDialog) {
        TwoButtonBottomSheetDialog(
            title = stringResource(id = R.string.clear_history_title),
            text = stringResource(id = R.string.clear_history_text),
            rightButtonText = stringResource(id = R.string.delete),
            onSubmit = clearHistory,
            onClose = { showClearHistoryDialog = false }
        )
    }
}

@Composable
fun HistoryEmptyResult() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(1.5f / 3f),
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

@Preview
@Composable
fun HistoryScreenPreview() {
    TripNNTheme {
        HistoryScreen(
            onBack = { },
            filterByWord = {},
            places = ResourceState(listOf(PLACE_1)),
            routes = ResourceState(ROUTES),
            removePlaceFromFavourite = {},
            removeRouteFromFavourite = {},
            addPlaceToFavourite = {},
            addRouteToFavourite = {},
            onTakeTheRoute = {},
            toPhotos = { _, _ -> },
            alreadyHasRoute = false,
            removePlaceFromHistory = {},
            removeRouteFromHistory = {},
            clearPlacesHistory = {},
            clearRoutesHistory = {}
        )
    }
}