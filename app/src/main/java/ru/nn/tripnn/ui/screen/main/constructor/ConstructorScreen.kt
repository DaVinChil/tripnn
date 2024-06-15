package ru.nn.tripnn.ui.screen.main.constructor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.R
import ru.nn.tripnn.data.datasource.stubdata.ui.PLACE_1
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.state.ResState
import ru.nn.tripnn.ui.common.InternetProblemScreen
import ru.nn.tripnn.ui.common.LoadingCircleScreen
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PlaceInfoBottomSheet
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.common.TwoButtonBottomSheetDialog
import ru.nn.tripnn.ui.common.card.PlaceCard
import ru.nn.tripnn.ui.common.card.RemoveFromRouteCardOption
import ru.nn.tripnn.ui.common.rippleClickable
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.screen.main.search.ConstructorSearchBottomSheet
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstructorScreen(
    onBack: () -> Unit,
    currentRoute: ResState<CurrentRoute?>,
    addPlace: (Place) -> Unit,
    removePlaceFromRoute: (Int) -> Unit,
    takeRoute: () -> Unit,
    toPhotos: (String, Int) -> Unit,
    removePlaceFromFavourite: (Place) -> Unit,
    addPlaceToFavourite: (Place) -> Unit,
    clearCurrentRoute: () -> Unit
) {
    var showDeleteCurrentRouteDialog by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ConstructorTopAppBar(
                scrollBehavior = scrollBehavior,
                onBack = onBack,
                onClearRoute = { showDeleteCurrentRouteDialog = true },
                showClearRouteAction = currentRoute.getOrNull()?.places?.isNotEmpty() == true
            )
        },
        containerColor = TripNnTheme.colorScheme.background
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
        ) {
            Column {
                if (currentRoute.isLoading()) {
                    LoadingCircleScreen()
                } else if (currentRoute.isError()) {
                    InternetProblemScreen()
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        RouteColumn(
                            currentRoute = currentRoute.getOrNull(),
                            removePlaceFromRoute = removePlaceFromRoute,
                            removePlaceFromFavourite = removePlaceFromFavourite,
                            addPlaceToFavourite = addPlaceToFavourite,
                            toPhotos = toPhotos,
                            addPlace = addPlace
                        )
                    }
                }
            }

            val isEnabled = currentRoute.getOrNull()?.places?.isNotEmpty() == true
            AnimatedVisibility(
                visible = isEnabled,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
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

            if (showDeleteCurrentRouteDialog) {
                TwoButtonBottomSheetDialog(
                    title = stringResource(id = R.string.delete_current_route_title),
                    text = stringResource(id = R.string.delete_current_route_text),
                    rightButtonText = stringResource(id = R.string.delete_current_route_right_button_text),
                    onSubmit = clearCurrentRoute,
                    onClose = { showDeleteCurrentRouteDialog = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstructorTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: () -> Unit,
    onClearRoute: () -> Unit,
    showClearRouteAction: Boolean
) {
    MediumTopAppBar(
        modifier = modifier
            .background(TripNnTheme.colorScheme.background),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TripNnTheme.colorScheme.background,
            scrolledContainerColor = TripNnTheme.colorScheme.background,
            titleContentColor = TripNnTheme.colorScheme.textColor
        ),
        title = {
            MontsText(
                text = stringResource(id = R.string.build_route),
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(id = R.string.back_txt),
                    tint = TripNnTheme.colorScheme.tertiary
                )
            }
        },
        actions = {
            if (showClearRouteAction) {
                IconButton(onClick = onClearRoute) {
                    Icon(
                        painter = painterResource(id = R.drawable.trashcan_small),
                        contentDescription = stringResource(id = R.string.delete),
                        tint = TripNnTheme.colorScheme.tertiary
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RouteColumn(
    currentRoute: CurrentRoute?,
    removePlaceFromRoute: (Int) -> Unit,
    removePlaceFromFavourite: (Place) -> Unit,
    addPlaceToFavourite: (Place) -> Unit,
    toPhotos: (String, Int) -> Unit,
    addPlace: (Place) -> Unit
) {
    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardInfo by remember { mutableStateOf(false) }
    var pickedPlace by remember { mutableStateOf(PLACE_1) }
    val density = LocalDensity.current
    val orderIds = remember {
        mutableListOf<Int>().apply {
            currentRoute?.places?.forEachIndexed { i, _ -> add(i) }
        }
    }

    LazyColumn(
        state = lazyState,
        contentPadding = PaddingValues(
            top = 10.dp,
            bottom = 90.dp + WindowInsets.systemBars.asPaddingValues(density)
                .calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(
            items = currentRoute?.places ?: listOf(),
            key = { index, place -> orderIds.getOrElse(index) { "$index|${place.id}" } }
        ) { index, place ->
            val option: @Composable () -> Unit =
                @Composable {
                    RemoveFromRouteCardOption(
                        onClick = {
                            removePlaceFromRoute(index)
                            orderIds.removeAt(index)
                        }
                    )
                }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.animateItemPlacement()
            ) {
                if (index > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MontsText(
                            text = (currentRoute?.walkInfo?.get(index - 1)?.timeToWalk ?: 0).toString()
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

                PlaceCard(
                    modifier = Modifier.fillMaxWidth(),
                    place = place,
                    onCardClick = {
                        pickedPlace = place
                        showCardInfo = true
                    },
                    option1 = option
                )
            }

        }

        item {
            AddPlaceButton(
                previousPlace = currentRoute?.places?.lastOrNull(),
                addPlace = {
                    orderIds.add((orderIds.lastOrNull() ?: 0) + 1)
                    addPlace(it)
                },
                toPhotos = toPhotos
            )
        }
    }

    if (showCardInfo) {
        PlaceInfoBottomSheet(
            onDismissRequest = { showCardInfo = false },
            sheetState = sheetState,
            place = pickedPlace,
            removeFromFavourite = { removePlaceFromFavourite(pickedPlace) },
            addToFavourite = { addPlaceToFavourite(pickedPlace) },
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
            .shadow(blurRadius = 8.dp, borderRadius = 8.dp)
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(TripNnTheme.colorScheme.primary)
            .rippleClickable {
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
            currentRoute = ResState.Success(CurrentRoute()),
            addPlace = {},
            removePlaceFromRoute = {},
            takeRoute = { /*TODO*/ },
            toPhotos = { _, _ -> },
            removePlaceFromFavourite = {},
            addPlaceToFavourite = {},
            clearCurrentRoute = {}
        )
    }
}