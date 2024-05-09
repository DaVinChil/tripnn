package ru.nn.tripnn.ui.screen.main.search

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.ui.common.DragHandle
import ru.nn.tripnn.ui.common.card.PlaceCard
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstructorSearchBottomSheet(
    onDismissRequest: () -> Unit,
    toPhotos: (String, Int) -> Unit,
    previousPlaceId: String?,
    onChoose: (Place) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val placesViewModel = hiltViewModel<AllPlacesViewModel>()
    val coroutine = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = TripNnTheme.colorScheme.bottomSheetBackground,
        sheetState = sheetState,
        dragHandle = { DragHandle() },
        windowInsets = WindowInsets(0)
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = SEARCH_ROUTE) {
            composable(
                route = SEARCH_ROUTE,
                exitTransition = { slideOutHorizontally { -it } },
                popEnterTransition = { slideInHorizontally { -it } }
            ) {
                SearchPlaceBuilderScreen(
                    onSearch = placesViewModel::search,
                    toResultScreen = {
                        navController.navigate(SEARCH_RESULT_ROUTE) {
                            launchSingleTop = true
                        }
                    },
                    previousPlaceId = previousPlaceId
                )
            }

            composable(
                route = SEARCH_RESULT_ROUTE,
                popExitTransition = { slideOutHorizontally { it } },
                enterTransition = { slideInHorizontally { it } }
            ) {
                ConstructorSearchResultScreen(
                    sort = placesViewModel::sort,
                    result = placesViewModel.searchResult,
                    removeFromFavourite = placesViewModel::removeFromFavourite,
                    addToFavourite = placesViewModel::addToFavourite,
                    popBack = navController::popBackStack,
                    toPhotos = toPhotos
                ) {
                    coroutine.launch {
                        sheetState.hide()
                    }.invokeOnCompletion { onDismissRequest() }
                    onChoose(it)
                }
            }
        }
    }
}

@Composable
fun ConstructorSearchResultScreen(
    sort: (SortState) -> Unit,
    result: ResourceState<List<Place>>,
    removeFromFavourite: (String) -> Unit,
    addToFavourite: (String) -> Unit,
    popBack: () -> Unit,
    toPhotos: (String, Int) -> Unit,
    onChoose: (Place) -> Unit
) {
    SearchResultScreen(
        sort = sort,
        result = result,
        removeFromFavourite = removeFromFavourite,
        addToFavourite = addToFavourite,
        popBack = popBack,
        toPhotos = toPhotos,
        onChoose = onChoose,
        buttonText = stringResource(id = R.string.add_place)
    )
}

@Composable
fun CardWithRadioButton(
    place: Place,
    option: @Composable () -> Unit,
    onCardClick: (Place) -> Unit,
    chosenPlace: Int,
    index: Int,
    onChoose: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlaceCard(
            place = place,
            onCardClick = { onCardClick(place) },
            shadowColor = Color.Black.copy(alpha = 0.2f),
            option1 = option,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            painter = if (chosenPlace == index) {
                painterResource(id = R.drawable.radio_button_selected)
            } else {
                painterResource(id = R.drawable.radio_button_not_selected)
            },
            contentDescription = if (chosenPlace == index) {
                stringResource(id = androidx.compose.ui.R.string.selected)
            } else {
                stringResource(id = androidx.compose.ui.R.string.not_selected)
            },
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onChoose(index) }
                ),
            tint = if (chosenPlace == index) {
                TripNnTheme.colorScheme.primary
            } else {
                TripNnTheme.colorScheme.onMinor
            }
        )
    }
}

@Preview
@Composable
fun ConstructorSearchPreview() {
    TripNNTheme {
        Box(modifier = Modifier.background(TripNnTheme.colorScheme.background)) {
            ConstructorSearchResultScreen(
                sort = {},
                result = ResourceState(listOf(PLACE_1)),
                removeFromFavourite = {},
                addToFavourite = {},
                popBack = { /*TODO*/ },
                toPhotos = { _, _ -> }
            ) {}
        }
    }
}