package ru.nn.tripnn.ui.screen.main.constructor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.ui.common.DraggableCard
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PlaceCard
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.common.RemoveFromRouteCardOption
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.common.LoadingCircleScreen
import ru.nn.tripnn.ui.common.InternetProblemScreen
import ru.nn.tripnn.ui.screen.main.search.ConstructorSearchBottomSheet
import ru.nn.tripnn.ui.common.PlaceInfoBottomSheet
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun ConstructorScreen(
    onBack: () -> Unit,
    currentRoute: ResourceState<CurrentRoute>,
    addPlace: (Place) -> Unit,
    removePlaceFromRoute: (Int) -> Unit,
    takeRoute: () -> Unit,
    toPhotos: (String, Int) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TripNnTheme.colorScheme.background)
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            .statusBarsPadding()
    ) {
        Column {
            IconButton(onClick = onBack, modifier = Modifier.offset(x = (-16).dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(id = R.string.back_txt),
                    tint = TripNnTheme.colorScheme.tertiary
                )
            }

            MontsText(
                text = stringResource(id = R.string.build_route),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(15.dp))

            if (currentRoute.isLoading) {
                LoadingCircleScreen()
            } else if (currentRoute.isError) {
                InternetProblemScreen()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    RouteColumn(
                        currentRoute = currentRoute.value,
                        removePlaceFromRoute = removePlaceFromRoute,
                        removePlaceFromFavourite = removePlaceFromFavourite,
                        addPlaceToFavourite = addPlaceToFavourite,
                        toPhotos = toPhotos,
                        addPlace = addPlace
                    )
                }
            }
        }

        val isEnabled = currentRoute.isSuccessAndNotNull() &&
                currentRoute.onNullFailCheck { places.isNotEmpty() }
        AnimatedVisibility(
            visible = isEnabled,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(bottom = 20.dp)
        ) {
            PrimaryButton(
                text = stringResource(R.string.take_the_route),
                onClick = takeRoute,
                enabled = isEnabled,
                containerColor = if (isEnabled) {
                    TripNnTheme.colorScheme.primary
                } else {
                    TripNnTheme.colorScheme.minor
                },
                textColor = if (isEnabled) {
                    TripNnTheme.colorScheme.onPrimary
                } else {
                    TripNnTheme.colorScheme.onMinor
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteColumn(
    currentRoute: CurrentRoute?,
    removePlaceFromRoute: (Int) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    toPhotos: (String, Int) -> Unit,
    addPlace: (Place) -> Unit
) {
    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardInfo by remember { mutableStateOf(false) }
    var pickedPlace by remember { mutableStateOf(PLACE_1) }
    val density = LocalDensity.current

    LazyColumn(
        state = lazyState,
        contentPadding = PaddingValues(
            top = 10.dp,
            bottom = 90.dp + WindowInsets.systemBars.asPaddingValues(
                density
            ).calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(
            items = currentRoute?.places ?: listOf(),
            key = { index, place -> "$index|${place.id}" }
        ) { index, place ->
            val option: @Composable () -> Unit =
                @Composable {
                    RemoveFromRouteCardOption(
                        onClick = { removePlaceFromRoute(index) }
                    )
                }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (index > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MontsText(
                            text = (place.timeToGo ?: 0).toString()
                                    + " " + stringResource(id = R.string.minute_short),
                            style = MaterialTheme.typography.labelSmall
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.steps),
                            contentDescription = "",
                            tint = TripNnTheme.colorScheme.onMinor
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
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

        item {
            AddPlaceButton(
                previousPlace = currentRoute?.places?.lastOrNull(),
                addPlace = addPlace,
                toPhotos = toPhotos
            )
        }
    }

    if (showCardInfo) {
        PlaceInfoBottomSheet(
            onDismissRequest = { showCardInfo = false },
            sheetState = sheetState,
            place = pickedPlace,
            removeFromFavourite = { removePlaceFromFavourite(pickedPlace.id) },
            addToFavourite = { addPlaceToFavourite(pickedPlace.id) },
            toPhotos = toPhotos
        )
    }
}

@Composable
fun AddPlaceButton(
    previousPlace: Place?,
    addPlace: (Place) -> Unit,
    toPhotos: (String, Int) -> Unit
) {
    var showPlaceSearch by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .shadow(color = Color.Black.copy(alpha = 0.3f), blurRadius = 8.dp, borderRadius = 8.dp)
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(TripNnTheme.colorScheme.primary)
            .clickable {
                showPlaceSearch = true
            }
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.Left
    ) {
        Icon(
            painter = painterResource(id = R.drawable.plus),
            contentDescription = stringResource(R.string.add_place),
            tint = TripNnTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.width(10.dp))

        MontsText(
            text = stringResource(id = R.string.add_place),
            style = MaterialTheme.typography.displayMedium,
            color = TripNnTheme.colorScheme.onPrimary
        )
    }

    if (showPlaceSearch) {
        ConstructorSearchBottomSheet(
            onDismissRequest = { showPlaceSearch = false },
            toPhotos = toPhotos,
            onChoose = addPlace,
            previousPlaceId = previousPlace?.id
        )
    }
}

@Preview
@Composable
fun ConstructorPreview() {
    TripNNTheme {
        ConstructorScreen(
            onBack = { /*TODO*/ },
            currentRoute = ResourceState(CurrentRoute()),
            addPlace = {},
            removePlaceFromRoute = {},
            takeRoute = { /*TODO*/ },
            toPhotos = { _, _ -> },
            removePlaceFromFavourite = {},
            addPlaceToFavourite = {}
        )
    }
}