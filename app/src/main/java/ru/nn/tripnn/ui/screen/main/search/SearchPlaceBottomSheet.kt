package ru.nn.tripnn.ui.screen.main.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.SearchFilters
import ru.nn.tripnn.ui.common.AddToFavouriteCardOption
import ru.nn.tripnn.ui.common.CatalogNavigation
import ru.nn.tripnn.ui.common.DragHandle
import ru.nn.tripnn.ui.common.DraggableCard
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PlaceCard
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.common.Rating
import ru.nn.tripnn.ui.common.RemoveFromFavouriteGoldCardOption
import ru.nn.tripnn.ui.common.Search
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.screen.main.favourite.LoadingCircleScreen
import ru.nn.tripnn.ui.screen.main.home.InternetProblem
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme
import java.time.LocalTime

val LEISURE_TYPES = listOf(
    R.string.zoos,
    R.string.river_trips,
    R.string.anti_cafe,
    R.string.attractions,
    R.string.karaoke,
    R.string.table_games,
    R.string.water_parks,
    R.string.circuses,
    R.string.quests,
    R.string.cinemas
)

val CULTURE_TYPES = listOf(
    R.string.museums,
    R.string.exhibitions,
    R.string.parks,
    R.string.theaters
)

val EAT_TYPES = listOf(
    R.string.home_cook,
    R.string.sushi_bars,
    R.string.pizzerias,
    R.string.fast_food,
    R.string.canteens,
    R.string.coffee_shops,
    R.string.bars,
    R.string.pastry_shops,
    R.string.shot_glasses,
    R.string.burgers,
    R.string.pyshechnye,
    R.string.khinkalnye,
    R.string.shawarma,
    R.string.dumplings,
)

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
                SearchPlaceScreen(
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
                    toPhotos = toPhotos,
                    onChoose = {
                        coroutine.launch {
                            sheetState.hide()
                        }.invokeOnCompletion { onDismissRequest() }
                        onChoose(it)
                    },
                    filters = placesViewModel.searchFilters,
                    previousPlaceId = previousPlaceId,
                    onSearch = placesViewModel::search
                )
            }
        }
    }
}

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
                SearchPlaceScreen(
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
                    toPhotos = toPhotos,
                    filters = placesViewModel.searchFilters,
                    onSearch = placesViewModel::search
                )
            }
        }
    }
}

@Composable
fun SearchPlaceScreen(
    onSearch: (SearchFilters) -> Unit,
    toResultScreen: () -> Unit,
    previousPlaceId: String? = null
) {
    val scrollState = rememberScrollState()
    var chosenCategory by remember { mutableIntStateOf(CULTURE) }
    var currentCategoryTypes by remember { mutableStateOf(CULTURE_TYPES) }
    var searchInput by remember { mutableStateOf("") }
    val catalogs = listOf(
        stringResource(id = R.string.culture),
        stringResource(id = R.string.leisure),
        stringResource(id = R.string.to_eat)
    )
    val pickedTypes = remember(chosenCategory) {
        mutableStateListOf<Boolean>().apply {
            for (i in currentCategoryTypes.indices) {
                add(false)
            }
        }
    }
    val pickedPrices = remember(chosenCategory) {
        mutableStateListOf<Boolean>().apply {
            repeat(4) {
                add(false)
            }
        }
    }
    var pickedMinRating by remember(chosenCategory) {
        mutableIntStateOf(-1)
    }
    var pickedDistance by remember(chosenCategory) {
        mutableIntStateOf(-1)
    }

    Box(
        modifier = Modifier
            .fillMaxHeight(5f / 6f)
            .background(TripNnTheme.colorScheme.bottomSheetBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .navigationBarsPadding()
                .padding(start = 15.dp, end = 15.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            CatalogNavigation(
                catalogs = catalogs,
                onCatalogChange = {
                    currentCategoryTypes = when (it) {
                        CULTURE -> CULTURE_TYPES
                        LEISURE -> LEISURE_TYPES
                        EAT -> EAT_TYPES
                        else -> listOf()
                    }
                    chosenCategory = it
                },
                chosen = chosenCategory
            )

            Search(modifier = Modifier.fillMaxWidth(), onSearch = { searchInput = it })

            PriceChoice(picked = pickedPrices, onPick = { pickedPrices[it] = !pickedPrices[it] })

            RatingChoice(picked = pickedMinRating, onPick = { pickedMinRating = it })

            DistanceChoice(picked = pickedDistance, onPick = { pickedDistance = it })

            TypeChoice(
                types = currentCategoryTypes,
                pickedTypes = pickedTypes,
                onPick = { pickedTypes[it] = !pickedTypes[it] },
                onPickAll = {
                    for (i in pickedTypes.indices) {
                        pickedTypes[i] = true
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .navigationBarsPadding()
        ) {
            PrimaryButton(
                text = stringResource(id = R.string.next),
                paddingValues = PaddingValues(horizontal = 58.dp, vertical = 15.dp),
                onClick = {
                    onSearch(
                        SearchFilters(
                            word = searchInput,
                            catalog = chosenCategory,
                            types = mutableListOf<Int>().apply {
                                pickedTypes.forEachIndexed { index, isPicked ->
                                    if (isPicked) add(currentCategoryTypes[index])
                                }
                            },
                            minPrice = pickedPrices.indexOf(true).let {
                                if (it == -1) null else it
                            },
                            maxPrice = pickedPrices.lastIndexOf(true).let {
                                if (it == -1) null else it
                            },
                            minRating = MIN_RATINGS.getOrNull(pickedMinRating),
                            previousPlaceId = previousPlaceId,
                            maxDistance = DISTANCES.getOrNull(pickedDistance),
                            userLocation = "",
                            workEndTime = LocalTime.now(),
                            workStartTime = LocalTime.now()
                        )
                    )
                    toResultScreen()
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PriceChoice(picked: List<Boolean>, onPick: (Int) -> Unit) {
    Column {
        MontsText(
            text = stringResource(id = R.string.avg_receipt),
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(13.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalArrangement = Arrangement.spacedBy(17.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            PRICES.forEachIndexed { i, price ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = if (picked[i]) TripNnTheme.colorScheme.primary else TripNnTheme.colorScheme.minor)
                        .clickable(
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            onClick = { onPick(i) }
                        )
                        .padding(horizontal = 15.dp, vertical = 11.dp)
                ) {
                    MontsText(
                        text = price,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (picked[i]) TripNnTheme.colorScheme.onPrimary else TripNnTheme.colorScheme.textColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RatingChoice(picked: Int, onPick: (Int) -> Unit) {
    Column {
        MontsText(
            text = stringResource(id = R.string.rating),
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(13.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            verticalArrangement = Arrangement.spacedBy(17.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            MIN_RATINGS.forEachIndexed { i, minRating ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = if (picked == i) TripNnTheme.colorScheme.primary else TripNnTheme.colorScheme.minor)
                        .clickable(
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            onClick = { onPick(i) }
                        )
                        .padding(horizontal = 11.dp, vertical = 11.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        MontsText(
                            text = stringResource(id = R.string.from_lower) + " $minRating",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (picked == i) TripNnTheme.colorScheme.onPrimary else TripNnTheme.colorScheme.textColor
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.outlined_gray_star),
                            contentDescription = stringResource(
                                id = R.string.rating_star
                            ),
                            tint = if (picked == i) TripNnTheme.colorScheme.onPrimary else TripNnTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f)
                        )
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DistanceChoice(picked: Int, onPick: (Int) -> Unit) {
    Column {
        MontsText(
            text = stringResource(id = R.string.distance),
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(13.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            verticalArrangement = Arrangement.spacedBy(17.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            DISTANCES.forEachIndexed { i, distance ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = if (picked == i) TripNnTheme.colorScheme.primary else TripNnTheme.colorScheme.minor)
                        .clickable(
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            onClick = { onPick(i) }
                        )
                        .padding(horizontal = 11.dp, vertical = 11.dp)
                ) {
                    MontsText(
                        text = stringResource(id = R.string.until) + " $distance " +
                                stringResource(id = R.string.km),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (picked == i) TripNnTheme.colorScheme.onPrimary else TripNnTheme.colorScheme.textColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TypeChoice(
    types: List<Int>,
    pickedTypes: List<Boolean>,
    onPick: (Int) -> Unit,
    onPickAll: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MontsText(
                text = stringResource(id = R.string.place_type),
                fontSize = 16.sp,
                style = MaterialTheme.typography.displayMedium
            )

            MontsText(
                text = stringResource(id = R.string.choose_all),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onPickAll
                )
            )
        }

        Spacer(modifier = Modifier.height(13.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            types.forEachIndexed { i, type ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = if (pickedTypes[i]) TripNnTheme.colorScheme.primary else TripNnTheme.colorScheme.minor)
                        .clickable(
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            onClick = { onPick(i) }
                        )
                        .padding(horizontal = 22.dp, vertical = 11.dp)
                ) {
                    MontsText(
                        text = stringResource(id = type),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (pickedTypes[i]) TripNnTheme.colorScheme.onPrimary else TripNnTheme.colorScheme.textColor
                    )
                }
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
    toPhotos: (String, Int) -> Unit,
    filters: SearchFilters,
    onSearch: (SearchFilters) -> Unit
) {
    SearchResultScreen(
        sort = sort,
        result = result,
        removeFromFavourite = removeFromFavourite,
        addToFavourite = addToFavourite,
        popBack = popBack,
        toPhotos = toPhotos,
        buttonText = null,
        onChoose = null,
        onSearch = onSearch,
        filters = filters,
        previousPlaceId = null
    )
}

@Composable
fun ConstructorSearchResultScreen(
    sort: (SortState) -> Unit,
    result: ResourceState<List<Place>>,
    removeFromFavourite: (String) -> Unit,
    addToFavourite: (String) -> Unit,
    popBack: () -> Unit,
    toPhotos: (String, Int) -> Unit,
    onChoose: (Place) -> Unit,
    filters: SearchFilters,
    previousPlaceId: String?,
    onSearch: (SearchFilters) -> Unit
) {
    SearchResultScreen(
        sort = sort,
        result = result,
        removeFromFavourite = removeFromFavourite,
        addToFavourite = addToFavourite,
        popBack = popBack,
        toPhotos = toPhotos,
        filters = filters,
        buttonText = stringResource(id = R.string.add_place),
        onChoose = onChoose,
        previousPlaceId = previousPlaceId,
        onSearch = onSearch
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
    onSearch: (SearchFilters) -> Unit,
    filters: SearchFilters,
    buttonText: String?,
    previousPlaceId: String?
) {
    if (result.isError) {
        InternetProblem()
        return
    }

    var sortState by remember { mutableStateOf(SortState(closer = false, byRating = true)) }
    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardInfo by remember { mutableStateOf(false) }
    var pickedPlace by remember { mutableStateOf(PLACE_1) }
    var chosenPlace by remember { mutableIntStateOf(-1) }
    var showSearchFilters by remember { mutableStateOf(false) }

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
                Icon(
                    painter = painterResource(id = R.drawable.filter_settings),
                    contentDescription = "",
                    tint = TripNnTheme.colorScheme.tertiary,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            showSearchFilters = true
                        }
                    )
                )
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

        if (showSearchFilters) {
            SearchFiltersScreen(
                filters = filters,
                onSearch = onSearch,
                previousPlaceId = previousPlaceId,
                onDismissRequest = { showSearchFilters = false }
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFiltersScreen(
    filters: SearchFilters,
    onSearch: (SearchFilters) -> Unit,
    previousPlaceId: String?,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutine = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val chosenCategory = filters.catalog ?: 0
    val currentCategoryTypes by remember {
        mutableStateOf(
            when (chosenCategory) {
                CULTURE -> CULTURE_TYPES
                LEISURE -> LEISURE_TYPES
                EAT -> EAT_TYPES
                else -> CULTURE_TYPES
            }
        )
    }
    val pickedTypes = remember {
        mutableStateListOf<Boolean>().apply {
            val types = filters.types?.toSet()
            if (types != null) {
                for (i in currentCategoryTypes.indices) {
                    add(types.contains(currentCategoryTypes[i]))
                }
            } else {
                for (i in currentCategoryTypes.indices) {
                    add(false)
                }
            }
        }
    }
    val pickedPrices = remember {
        mutableStateListOf<Boolean>().apply {
            val minPrice = filters.minPrice
            val maxPrice = filters.maxPrice

            repeat(4) {
                add(minPrice == it || maxPrice == it)
            }
        }
    }
    var pickedMinRating by remember {
        mutableIntStateOf(MIN_RATINGS.indexOf(filters.minRating))
    }
    var pickedDistance by remember {
        mutableIntStateOf(DISTANCES.indexOf(filters.maxDistance))
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = TripNnTheme.colorScheme.bottomSheetBackground,
        dragHandle = { DragHandle() },
        windowInsets = WindowInsets(0)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(5f / 6f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .navigationBarsPadding()
                    .padding(start = 15.dp, end = 15.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                PriceChoice(
                    picked = pickedPrices,
                    onPick = { pickedPrices[it] = !pickedPrices[it] })

                RatingChoice(picked = pickedMinRating, onPick = { pickedMinRating = it })

                DistanceChoice(picked = pickedDistance, onPick = { pickedDistance = it })

                TypeChoice(
                    types = currentCategoryTypes,
                    pickedTypes = pickedTypes,
                    onPick = { pickedTypes[it] = !pickedTypes[it] },
                    onPickAll = {
                        for (i in pickedTypes.indices) {
                            pickedTypes[i] = true
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .navigationBarsPadding()
            ) {
                PrimaryButton(
                    text = stringResource(id = R.string.apply),
                    paddingValues = PaddingValues(horizontal = 58.dp, vertical = 15.dp),
                    onClick = {
                        onSearch(
                            SearchFilters(
                                catalog = chosenCategory,
                                types = mutableListOf<Int>().apply {
                                    pickedTypes.forEachIndexed { index, isPicked ->
                                        if (isPicked) add(currentCategoryTypes[index])
                                    }
                                },
                                minPrice = pickedPrices.indexOf(true).let {
                                    if (it == -1) null else it
                                },
                                maxPrice = pickedPrices.lastIndexOf(true).let {
                                    if (it == -1) null else it
                                },
                                minRating = MIN_RATINGS.getOrNull(pickedMinRating),
                                previousPlaceId = previousPlaceId,
                                maxDistance = DISTANCES.getOrNull(pickedDistance),
                                userLocation = "",
                                workEndTime = LocalTime.now(),
                                workStartTime = LocalTime.now()
                            )
                        )

                        coroutine.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            onDismissRequest()
                        }
                    }
                )
            }
        }
    }
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
        DraggableCard(option1 = option, modifier = Modifier.weight(1f)) {
            PlaceCard(
                place = place,
                onCardClick = { onCardClick(place) },
                shadowColor = Color.Black.copy(alpha = 0.2f)
            )
        }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceInfoBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    place: Place,
    removeFromFavourite: () -> Unit,
    addToFavourite: () -> Unit,
    toPhotos: (String, Int) -> Unit
) {
    var showSnackBar by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { DragHandle() },
        containerColor = Color.Transparent,
        windowInsets = WindowInsets(0)
    ) {
        Box {
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    itemsIndexed(items = place.photos, key = { _, url -> url }) { index, url ->
                        AsyncImage(
                            model = url,
                            contentDescription = stringResource(id = R.string.image),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(TripNnTheme.colorScheme.onMinor)
                                .width(220.dp)
                                .height(160.dp)
                                .clickable {
                                    toPhotos(place.id, index)
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                topStart = 15.dp, topEnd = 15.dp,
                                bottomEnd = 0.dp, bottomStart = 0.dp
                            )
                        )
                        .background(TripNnTheme.colorScheme.bottomSheetBackground)
                        .padding(top = 10.dp)
                        .navigationBarsPadding()
                ) {
                    var favourite by rememberSaveable { mutableStateOf(place.favourite) }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        MontsText(
                            text = place.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        favourite = if (favourite) {
                                            removeFromFavourite()
                                            false
                                        } else {
                                            addToFavourite()
                                            true
                                        }
                                    }
                                ),
                            painter = painterResource(
                                id = if (favourite) R.drawable.gold_bookmark
                                else R.drawable.gray_bookmark
                            ),
                            contentDescription = stringResource(id = R.string.is_favourtie),
                            tint = Color.Unspecified
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.fillMaxHeight()) {
                            if (place.type != null) {
                                MontsText(
                                    text = place.type,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            TwoGisButton(doubleGisLink = place.doubleGisLink)

                            Spacer(modifier = Modifier.height(5.dp))

                            Row {
                                Rating(rating = place.rating, style = MaterialTheme.typography.bodySmall)

                                Spacer(modifier = Modifier.width(5.dp))

                                MontsText(
                                    text = place.reviews.toString() + " " +
                                            stringResource(id = R.string.reviews),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {

                            if (place.address != null) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    MontsText(
                                        text = place.address,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    CopyIcon(
                                        data = place.address,
                                        onClick = { showSnackBar = true }
                                    )
                                }
                            }

                            if (place.workTime != null) {
                                MontsText(
                                    text = place.workTime,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            if (place.phone != null) {
                                MontsText(
                                    text = place.phone,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            MontsText(
                                text = stringResource(id = R.string.avg_receipt) + " " + place.cost + "₽",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            if (showSnackBar) {
                CopiedToClipBoardSnackBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onHide = { showSnackBar = false })
            }
        }
    }
}

@Composable
fun CopyIcon(data: String, onClick: () -> Unit) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    Icon(
        painter = painterResource(id = R.drawable.copy_icon),
        contentDescription = stringResource(id = R.string.copy),
        tint = TripNnTheme.colorScheme.onMinor,
        modifier = Modifier.clickable(
            indication = rememberRipple(radius = 1.dp, color = Color.White),
            interactionSource = remember { MutableInteractionSource() },
            onClick = {
                clipboardManager.setText(buildAnnotatedString { append(data) })
                onClick()
            }
        )
    )
}

@Composable
fun CopiedToClipBoardSnackBar(modifier: Modifier = Modifier, onHide: () -> Unit) {
    val startOffset = 80f
    val offset = remember { Animatable(startOffset) }
    val alpha = remember { Animatable(0f) }
    val localOnHide by rememberUpdatedState(newValue = onHide)
    val time = 2000L

    Row(
        modifier = Modifier
            .offset(y = offset.value.dp - 10.dp)
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .alpha(alpha.value)
            .then(modifier)
            .fillMaxWidth()
            .background(TripNnTheme.colorScheme.primary)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.info),
            contentDescription = stringResource(id = R.string.info),
            tint = TripNnTheme.colorScheme.onPrimary
        )
        MontsText(
            text = stringResource(id = R.string.address_copied),
            style = MaterialTheme.typography.labelMedium,
            color = TripNnTheme.colorScheme.onPrimary
        )
    }

    LaunchedEffect(Unit) {
        launch { alpha.animateTo(1f, tween(500)) }
        offset.animateTo(
            0f,
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        delay(time)

        launch { alpha.animateTo(0f, tween(300)) }
        offset.animateTo(
            startOffset,
            tween(500)
        )

        localOnHide()
    }
}

@Composable
fun TwoGisButton(modifier: Modifier = Modifier, doubleGisLink: String) {
    val uriHandler = LocalUriHandler.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { uriHandler.openUri(doubleGisLink) }
    ) {
        MontsText(
            text = "2GIS",
            style = MaterialTheme.typography.labelMedium
        )
        Icon(
            painter = painterResource(id = R.drawable.reversed_link_icon),
            contentDescription = "",
            tint = TripNnTheme.colorScheme.tertiary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CardInfoBottomSheetPreview() {
    TripNNTheme {
        Box(modifier = Modifier.background(TripNnTheme.colorScheme.background)) {
            PlaceInfoBottomSheet(
                place = PLACE_1,
                removeFromFavourite = { },
                addToFavourite = {},
                sheetState = rememberModalBottomSheetState(),
                onDismissRequest = {},
                toPhotos = { _, _ -> }
            )
        }
    }
}

@Preview
@Composable
fun SearchPlaceContentPreview() {
    TripNNTheme() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TripNnTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            SearchPlaceScreen(onSearch = {}, toResultScreen = {})
        }
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
                toPhotos = { _, _ -> },
                onChoose = {},
                filters = SearchFilters(),
                previousPlaceId = "",
                onSearch = {}
            )
        }
    }
}