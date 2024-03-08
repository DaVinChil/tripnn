package ru.nn.tripnn.ui.screen.main.recommendations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.ui.common.DragHandle
import ru.nn.tripnn.ui.common.DraggableCard
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.RemoveFromFavouriteRedCardOption
import ru.nn.tripnn.ui.common.RouteCard
import ru.nn.tripnn.ui.common.Search
import ru.nn.tripnn.ui.screen.main.favourite.ResourceListState
import ru.nn.tripnn.ui.screen.main.favourite.RouteInfoBottomSheetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(
    onBack: () -> Unit,
    filterRoutes: (String) -> Unit,
    routes: ResourceListState<Route>,
    removeRouteFromFavourite: (String) -> Unit,
    addRouteToFavourite: (String) -> Unit,
    removePlaceFromFavourite: (String) -> Unit,
    addPlaceToFavourite: (String) -> Unit
) {
    var word by remember { mutableStateOf("") }

    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showRouteInfo by remember { mutableStateOf(false) }
    var pickedRoute by remember { mutableStateOf(routes.list[0]) }

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
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }

        MontsText(
            text = "Рекомендованные маршруты",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Search(
            onSearch = {
                word = it
                filterRoutes(word)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            state = lazyState,
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(items = routes.list, key = Route::id) { route ->
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

        if (showRouteInfo) {
            ModalBottomSheet(
                onDismissRequest = { showRouteInfo = false },
                sheetState = sheetState,
                dragHandle = { DragHandle() },
                containerColor = MaterialTheme.colorScheme.background
            ) {
                RouteInfoBottomSheetContent(
                    removeRouteFromFavourite = { removeRouteFromFavourite(pickedRoute.id) },
                    addRouteToFavourite = { addRouteToFavourite(pickedRoute.id) },
                    removePlaceFromFavourite = removePlaceFromFavourite,
                    addPlaceToFavourite = addPlaceToFavourite,
                    route = pickedRoute,
                    onTakeTheRoute = { TODO() }
                )
            }
        }
    }
}