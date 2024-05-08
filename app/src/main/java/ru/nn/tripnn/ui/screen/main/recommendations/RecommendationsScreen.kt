package ru.nn.tripnn.ui.screen.main.recommendations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.ROUTE_1
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.common.DragHandle
import ru.nn.tripnn.ui.common.DraggableCard
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.RemoveFromFavouriteRedCardOption
import ru.nn.tripnn.ui.common.RouteCard
import ru.nn.tripnn.ui.common.Search
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.common.LoadingCircleScreen
import ru.nn.tripnn.ui.common.RouteInfoBottomSheetContent
import ru.nn.tripnn.ui.common.InternetProblemScreen
import ru.nn.tripnn.ui.theme.TripNnTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(
    onBack: () -> Unit,
    isEmpty: Boolean,
    filterRoutes: (String) -> Unit,
    routes: ResourceState<List<Route>>,
    removeRouteFromFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit,
    toPhotos: (String, Int) -> Unit,
    onTakeTheRoute: (Route) -> Unit,
    alreadyHasRoute: Boolean
) {
    if (routes.isError) {
        InternetProblemScreen()
        return
    }

    if (isEmpty && !routes.isLoading) {
        NoRecommendedRoutes(onBack)
        return
    }

    var word by remember { mutableStateOf("") }

    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showRouteInfo by remember { mutableStateOf(false) }
    var pickedRoute by remember { mutableStateOf(ROUTE_1) }

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
            text = stringResource(id = R.string.recommended_routes),
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (routes.isLoading) {
            LoadingCircleScreen()
            return
        }

        Search(
            onSearch = {
                word = it
                filterRoutes(word)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (routes.value.isNullOrEmpty()) {
            NothingFoundByRequest()
        } else {
            LazyColumn(
                state = lazyState,
                contentPadding = PaddingValues(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(items = routes.value, key = Route::id) { route ->
                    val option: @Composable () -> Unit =
                        @Composable {
                            RemoveFromFavouriteRedCardOption(
                                onClick = { removeRouteFromFavourite(route.id) })
                        }
                    DraggableCard(option1 = option) {
                        RouteCard(
                            route = route,
                            onCardClick = {
                                pickedRoute = route
                                showRouteInfo = true
                            },
                            shadowColor = Color.Black.copy(alpha = 0.2f),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        if (showRouteInfo) {
            ModalBottomSheet(
                onDismissRequest = { showRouteInfo = false },
                sheetState = sheetState,
                dragHandle = { DragHandle() },
                containerColor = TripNnTheme.colorScheme.bottomSheetBackground,
                windowInsets = WindowInsets(0)
            ) {
                RouteInfoBottomSheetContent(
                    removeRouteFromFavourite = { removeRouteFromFavourite(pickedRoute.id) },
                    addRouteToFavourite = { addRouteToFavourite(pickedRoute.id) },
                    removePlaceFromFavourite = removePlaceFromFavourite,
                    addPlaceToFavourite = addPlaceToFavourite,
                    route = pickedRoute,
                    onTakeTheRoute = onTakeTheRoute,
                    toPhotos = toPhotos,
                    alreadyHasRoute = alreadyHasRoute
                )
            }
        }
    }
}

@Composable
fun NothingFoundByRequest() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(2 / 3f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MontsText(text = "☹\uFE0F", fontSize = 50.sp)
            Spacer(modifier = Modifier.height(35.dp))
            MontsText(
                text = stringResource(id = R.string.nothing_found_header),
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NoRecommendedRoutes(onBack: () -> Unit) {
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
            text = stringResource(id = R.string.recommended_routes),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
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
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(35.dp))
                MontsText(text = "☹\uFE0F", fontSize = 50.sp)
            }
        }
    }
}
