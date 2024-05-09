package ru.nn.tripnn.ui.screen.main.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.ui.common.DragHandle
import ru.nn.tripnn.ui.common.InternetProblemScreen
import ru.nn.tripnn.ui.common.LoadingCircleScreen
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PlaceInfoBottomSheet
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.common.Search
import ru.nn.tripnn.ui.common.card.AddToFavouriteCardOption
import ru.nn.tripnn.ui.common.card.PlaceCard
import ru.nn.tripnn.ui.common.card.RemoveFromFavouriteGoldCardOption
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.theme.TripNnTheme

val MIN_RATINGS = listOf(3f, 3.5f, 4f, 4.5f)
val PRICES = listOf("₽", "₽₽", "₽₽₽", "₽₽₽₽")
val DISTANCES = listOf(1, 2, 5, 10)

const val LEISURE = 1
const val CULTURE = 0
const val EAT = 2

const val SEARCH_ROUTE = "search"
const val SEARCH_RESULT_ROUTE = "search_result"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPlaceBottomSheet(
    onDismissRequest: () -> Unit,
    toPhotos: (String, Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val placesViewModel = hiltViewModel<AllPlacesViewModel>()

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
                    }
                )
            }

            composable(
                route = SEARCH_RESULT_ROUTE,
                popExitTransition = { slideOutHorizontally { it } },
                enterTransition = { slideInHorizontally { it } }
            ) {
                SearchResultScreen(
                    sort = placesViewModel::sort,
                    result = placesViewModel.searchResult,
                    removeFromFavourite = placesViewModel::removeFromFavourite,
                    addToFavourite = placesViewModel::addToFavourite,
                    popBack = navController::popBackStack,
                    toPhotos = toPhotos
                )
            }
        }
    }
}

@Composable
fun SearchResultScreen(
    sort: (SortState) -> Unit,
    result: ResourceState<List<Place>>,
    removeFromFavourite: (String) -> Unit,
    addToFavourite: (String) -> Unit,
    popBack: () -> Unit,
    toPhotos: (String, Int) -> Unit
) {
    SearchResultScreen(
        sort = sort,
        result = result,
        removeFromFavourite = removeFromFavourite,
        addToFavourite = addToFavourite,
        popBack = popBack,
        toPhotos = toPhotos,
        onChoose = null,
        buttonText = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    sort: (SortState) -> Unit,
    result: ResourceState<List<Place>>,
    removeFromFavourite: (String) -> Unit,
    addToFavourite: (String) -> Unit,
    popBack: () -> Unit,
    toPhotos: (String, Int) -> Unit,
    onChoose: ((Place) -> Unit)?,
    buttonText: String?
) {
    if (result.isError) {
        InternetProblemScreen()
        return
    }

    var sortState by remember { mutableStateOf(SortState(closer = false, byRating = true)) }
    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardInfo by remember { mutableStateOf(false) }
    var pickedPlace by remember { mutableStateOf(PLACE_1) }
    var chosenPlace by remember { mutableIntStateOf(-1) }

    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(5f / 6f)
                .padding(horizontal = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = popBack, modifier = Modifier.offset(x = (-16).dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = stringResource(id = R.string.back_txt),
                        tint = TripNnTheme.colorScheme.onMinor
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                MontsText(
                    text = stringResource(id = R.string.closer),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (sortState.closer) TripNnTheme.colorScheme.primary else TripNnTheme.colorScheme.onMinor,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        sortState = sortState.copy(closer = true, byRating = false)
                        sort(sortState)
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                MontsText(
                    text = stringResource(id = R.string.by_rating),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (sortState.byRating) TripNnTheme.colorScheme.primary else TripNnTheme.colorScheme.onMinor,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        sortState = sortState.copy(closer = false, byRating = true)
                        sort(sortState)
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
            }

            Search(
                modifier = Modifier.fillMaxWidth(),
                onSearch = { sortState = sortState.copy(word = it); sort(sortState) })

            Spacer(modifier = Modifier.height(20.dp))

            if (result.isLoading) {
                Box(modifier = Modifier.fillMaxHeight(5f / 6f)) {
                    LoadingCircleScreen()
                }
            } else if (result.value.isNullOrEmpty()) {
                SearchEmptyResult(popBack)
            } else {
                LazyColumn(state = lazyState, contentPadding = PaddingValues(vertical = 10.dp)) {
                    itemsIndexed(
                        items = result.value,
                        key = { _, place -> place.id }) { index, place ->
                        val option: @Composable () -> Unit = if (place.favourite) {
                            @Composable {
                                RemoveFromFavouriteGoldCardOption(
                                    onClick = { removeFromFavourite(place.id) }
                                )
                            }
                        } else {
                            @Composable {
                                AddToFavouriteCardOption(onClick = { addToFavourite(place.id) })
                            }
                        }

                        if (onChoose != null) {
                            CardWithRadioButton(
                                place = place,
                                option = option,
                                onCardClick = {
                                    pickedPlace = place
                                    showCardInfo = true
                                },
                                chosenPlace = chosenPlace,
                                index = index,
                                onChoose = {
                                    chosenPlace = index
                                }
                            )
                        } else {
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
                }
            }
        }

        if (buttonText != null && onChoose != null) {
            AnimatedVisibility(
                visible = chosenPlace != -1,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .navigationBarsPadding(),
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutHorizontally { it } + fadeOut()
            ) {
                PrimaryButton(
                    text = stringResource(id = R.string.add_place),
                    paddingValues = PaddingValues(horizontal = 58.dp, vertical = 15.dp),
                    onClick = { if (chosenPlace != -1 && result.value != null) onChoose(result.value[chosenPlace]) }
                )
            }
        }

        if (showCardInfo) {
            PlaceInfoBottomSheet(
                onDismissRequest = { showCardInfo = false },
                sheetState = sheetState,
                place = pickedPlace,
                removeFromFavourite = { removeFromFavourite(pickedPlace.id) },
                addToFavourite = { addToFavourite(pickedPlace.id) },
                toPhotos = toPhotos
            )
        }
    }
}

@Composable
fun SearchEmptyResult(popBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(5f / 6f)
            .padding(horizontal = 10.dp)
    ) {
        IconButton(onClick = popBack, modifier = Modifier.offset(x = (-16).dp)) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = stringResource(id = R.string.back_txt),
                tint = TripNnTheme.colorScheme.onMinor
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(3 / 5f)
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MontsText(
                text = stringResource(id = R.string.nothing_found_header),
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(50.dp))
            MontsText(text = "☹\uFE0F", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(50.dp))
            MontsText(
                text = stringResource(id = R.string.nothing_found_comment),
                style = MaterialTheme.typography.displayLarge
            )
        }
    }
}

