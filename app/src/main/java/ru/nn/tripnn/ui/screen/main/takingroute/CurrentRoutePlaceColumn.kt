package ru.nn.tripnn.ui.screen.main.takingroute

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.R
import ru.nn.tripnn.data.datasource.stubdata.ui.CURRENT_ROUTE
import ru.nn.tripnn.data.datasource.stubdata.ui.PLACE_1
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PlaceInfoBottomSheet
import ru.nn.tripnn.ui.common.card.AddToFavouriteCardOption
import ru.nn.tripnn.ui.common.card.PlaceCard
import ru.nn.tripnn.ui.common.card.RemoveFromFavouriteGoldCardOption
import ru.nn.tripnn.ui.common.card.RemoveFromRouteCardOption
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CurrentRoutePlaceColumn(
    modifier: Modifier = Modifier,
    currentRoute: CurrentRoute,
    addPlaceToFavourite: (Place) -> Unit,
    removePlaceFromFavourite: (Place) -> Unit,
    removePlaceFromRoute: (Int) -> Unit,
    toPlacePhotos: (String, Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val listState = rememberLazyListState()

    LaunchedEffect(currentRoute.currentPlaceIndex) {
        listState.animateScrollToItem(currentRoute.currentPlaceIndex)
    }

    var showCardInfo by remember { mutableStateOf(false) }
    var pickedPlace by remember { mutableStateOf(PLACE_1) }

    val orderIds = remember(currentRoute.places.size) {
        (0 until currentRoute.places.size).toMutableList()
    }
    val getKey = remember(orderIds) {
        { index: Int, _: Place ->
            orderIds[index]
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(
            top = 10.dp,
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 30.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(
            items = currentRoute.places,
            key = getKey
        ) { index, place ->
            val option2 = ensureAbleToRemoveFromRoute(currentRoute, index) {
                removePlaceFromRoute(index)
                orderIds.removeAt(index)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.animateItemPlacement()
            ) {
                if (index > 0) {
                    if (index == currentRoute.currentPlaceIndex) {
                        CurrentPlaceLine()
                    } else {
                        TimeToWalkInfo(timeToWalk = currentRoute.walkInfo[index - 1].timeToWalk)
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                PlaceCard(
                    modifier = Modifier.fillMaxWidth(),
                    place = place,
                    onCardClick = {
                        pickedPlace = place
                        showCardInfo = true
                    },
                    grayPhoto = index < currentRoute.currentPlaceIndex,
                    option1 = addOrRemovePlaceFromFavouriteOption(
                        place,
                        removePlaceFromFavourite,
                        addPlaceToFavourite
                    ),
                    option2 = option2
                )
            }
        }
    }

    if (showCardInfo) {
        PlaceInfoBottomSheet(
            onDismissRequest = { showCardInfo = false },
            sheetState = sheetState,
            place = pickedPlace,
            removeFromFavourite = { removePlaceFromFavourite(pickedPlace) },
            addToFavourite = { addPlaceToFavourite(pickedPlace) },
            toPhotos = toPlacePhotos
        )
    }
}

fun ensureAbleToRemoveFromRoute(
    currentRoute: CurrentRoute,
    index: Int,
    removePlaceFromFavourite: () -> Unit
): @Composable (() -> Unit)? {
    return if (
        index >= currentRoute.currentPlaceIndex &&
        currentRoute.places.lastIndex != currentRoute.currentPlaceIndex
    ) {
        @Composable {
            RemoveFromRouteCardOption(
                onClick = removePlaceFromFavourite
            )
        }
    } else null
}

@Composable
fun CurrentPlaceLine(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(TripNnTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Icon(
            painter = painterResource(id = R.drawable.flag),
            contentDescription = "flag",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(20.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(TripNnTheme.colorScheme.primary)
        )
    }
}

private fun addOrRemovePlaceFromFavouriteOption(
    place: Place,
    removePlaceFromFavourite: (Place) -> Unit,
    addPlaceToFavourite: (Place) -> Unit
): @Composable (() -> Unit) {
    return if (place.favourite) {
        @Composable {
            RemoveFromFavouriteGoldCardOption(
                onClick = { removePlaceFromFavourite(place) }
            )
        }
    } else {
        @Composable {
            AddToFavouriteCardOption(onClick = { addPlaceToFavourite(place) })
        }
    }
}

@Composable
fun TimeToWalkInfo(modifier: Modifier = Modifier, timeToWalk: Int) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MontsText(
            text = (timeToWalk.toString() + " " + stringResource(id = R.string.minute_short)),
            style = MaterialTheme.typography.labelSmall
        )

        Icon(
            painter = painterResource(id = R.drawable.steps),
            contentDescription = "",
            tint = TripNnTheme.colorScheme.onMinor
        )
    }
}

@Preview
@Composable
private fun CurrentRoutePlaceColumnPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(TripNnTheme.colorScheme.background)
        ) {
            CurrentRoutePlaceColumn(
                currentRoute = CURRENT_ROUTE,
                addPlaceToFavourite = {},
                removePlaceFromFavourite = {},
                removePlaceFromRoute = {},
                toPlacePhotos = { _, _ -> }
            )
        }
    }
}