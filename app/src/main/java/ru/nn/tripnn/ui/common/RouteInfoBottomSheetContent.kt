package ru.nn.tripnn.ui.common

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.ROUTE_1
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.common.card.AddToFavouriteCardOption
import ru.nn.tripnn.ui.common.card.PlaceCard
import ru.nn.tripnn.ui.common.card.RemoveFromFavouriteGoldCardOption
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteInfoBottomSheetContent(
    route: Route,
    removeRouteFromFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    onTakeTheRoute: (Route) -> Unit,
    toPhotos: (String, Int) -> Unit,
    alreadyHasRoute: Boolean
) {
    var favourite by rememberSaveable { mutableStateOf(route.favourite) }
    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardInfo by rememberSaveable { mutableStateOf(false) }
    var pickedPlace by rememberSaveable { mutableIntStateOf(0) }
    var showReplaceCurrentRouteDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(5f / 6f)
            .navigationBarsPadding()
            .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                MontsText(
                    text = route.name,
                    style = MaterialTheme.typography.headlineMedium,
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
                        Rating(route.rating, style = MaterialTheme.typography.headlineMedium)
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = TripNnTheme.colorScheme.onMinor
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn(
                state = lazyState,
                contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(
                    items = route.places,
                    key = { _, place -> place.id }) { index, place ->
                    val option: @Composable () -> Unit = if (place.favourite) {
                        @Composable {
                            RemoveFromFavouriteGoldCardOption(
                                onClick = { removePlaceFromFavourite(place.id) })
                        }
                    } else {
                        @Composable { AddToFavouriteCardOption(onClick = { addPlaceToFavourite(place.id) }) }
                    }
                    PlaceCard(
                        modifier = Modifier.fillMaxWidth(),
                        place = place,
                        onCardClick = { showCardInfo = true; pickedPlace = index },
                        option1 = option
                    )
                }
            }
        }

        PrimaryButton(
            text = stringResource(id = R.string.take_the_route),
            onClick = {
                if (alreadyHasRoute) {
                    showReplaceCurrentRouteDialog = true
                } else {
                    onTakeTheRoute(route)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    if (showReplaceCurrentRouteDialog) {
        TwoButtonBottomSheetDialog(
            title = stringResource(id = R.string.delete_current_route_title),
            text = stringResource(id = R.string.delete_current_route_text),
            rightButtonText = stringResource(id = R.string.delete_current_route_right_button_text),
            onSubmit = { onTakeTheRoute(route) },
            onClose = { showReplaceCurrentRouteDialog = false }
        )
    }

    if (showCardInfo) {
        PlaceInfoBottomSheet(
            place = route.places[pickedPlace],
            sheetState = sheetState,
            onDismissRequest = { showCardInfo = false },
            removeFromFavourite = { removePlaceFromFavourite(route.places[pickedPlace].id) },
            addToFavourite = { addPlaceToFavourite(route.places[pickedPlace].id) },
            toPhotos = toPhotos
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
                .background(TripNnTheme.colorScheme.background)
        ) {
            RouteInfoBottomSheetContent(
                route = ROUTE_1,
                removeRouteFromFavourite = { },
                addRouteToFavourite = { },
                removePlaceFromFavourite = {},
                addPlaceToFavourite = {},
                onTakeTheRoute = {},
                toPhotos = { _, _ -> },
                alreadyHasRoute = false
            )
        }
    }
}