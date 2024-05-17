package ru.nn.tripnn.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.data.datasource.stubdata.ui.ROUTE_1
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.common.card.RemoveFromFavouriteGoldCardOption
import ru.nn.tripnn.ui.common.card.RouteCard
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.theme.TripNnTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesColumn(
    routes: ResourceState<List<Route>>,
    onEmpty: @Composable () -> Unit,
    removeRouteFromFavourite: (Route) -> Unit,
    addRouteToFavourite: (Route) -> Unit,
    removePlaceFromFavourite: (Place) -> Unit,
    addPlaceToFavourite: (Place) -> Unit,
    onTakeTheRoute: (Route) -> Unit,
    toPhotos: (String, Int) -> Unit,
    alreadyHasRoute: Boolean,
    option2: @Composable ((Route) -> Unit)? = null
) {
    if (routes.isError) {
        InternetProblemScreen()
        return
    }

    if (routes.isLoading) {
        LoadingCircleScreen()
        return
    }

    if (routes.state.isNullOrEmpty()) {
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
        items(items = routes.state, key = { it.remoteId ?: it.hashCode() }) { route ->
            val option: @Composable () -> Unit =
                @Composable {
                    RemoveFromFavouriteGoldCardOption(
                        onClick = { removeRouteFromFavourite(route) })
                }
            RouteCard(
                route = route,
                onCardClick = {
                    pickedRoute = route
                    showRouteInfo = true
                },
                modifier = Modifier.fillMaxWidth(),
                option1 = option,
                option2 = if (option2 != null) {
                    @Composable { option2(route)}
                } else {
                    null
                }
            )
        }
    }

    if (showRouteInfo) {
        ModalBottomSheet(
            onDismissRequest = { showRouteInfo = false },
            sheetState = sheetState,
            containerColor = TripNnTheme.colorScheme.bottomSheetBackground,
            dragHandle = { DragHandle() },
            windowInsets = WindowInsets(0)
        ) {
            RouteInfoBottomSheetContent(
                route = pickedRoute,
                removeRouteFromFavourite = { removeRouteFromFavourite(pickedRoute) },
                addRouteToFavourite = { addRouteToFavourite(pickedRoute) },
                removePlaceFromFavourite = removePlaceFromFavourite,
                addPlaceToFavourite = addPlaceToFavourite,
                onTakeTheRoute = onTakeTheRoute,
                toPhotos = toPhotos,
                alreadyHasRoute = alreadyHasRoute
            )
        }
    }
}