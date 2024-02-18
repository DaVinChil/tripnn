package ru.nn.tripnn.ui.screen.main.search

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
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
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.PLACE_FULL_1
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.PlaceFull
import ru.nn.tripnn.ui.common.AddToFavouriteCardOption
import ru.nn.tripnn.ui.common.CatalogNavigation
import ru.nn.tripnn.ui.common.DraggableCard
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PlaceCard
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.common.RemoveFromFavouriteGoldCardOption
import ru.nn.tripnn.ui.common.Search
import ru.nn.tripnn.ui.theme.TripNNTheme

val TYPES = listOf(
    listOf(
        R.string.all_types,
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
    ),
    listOf(
        R.string.all_types,
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
    ),
    listOf(
        R.string.all_types,
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
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPlaceBottomSheet(onDismissRequest: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val placesViewModel = hiltViewModel<AllPlacesViewModel>()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "search") {
            composable(
                route = "search",
                exitTransition = { slideOutHorizontally { -it } },
                popEnterTransition = { slideInHorizontally { -it } }
            ) {
                SearchPlaceScreen(onSearch = placesViewModel::search, toResultScreen = {
                    navController.navigate("search_result") {
                        launchSingleTop = true
                    }
                })
            }
            composable(
                route = "search_result",
                popExitTransition = { slideOutHorizontally { it } },
                enterTransition = { slideInHorizontally { it } }
            ) {
                SearchResultScreen(
                    sort = placesViewModel::sort,
                    result = placesViewModel.searchResult,
                    removeFromFavourite = placesViewModel::removeFromFavourite,
                    addToFavourite = placesViewModel::addToFavourite,
                    popBack = navController::popBackStack,
                    getFullPlaceInfo = placesViewModel::getFullInfo,
                    placeFull = placesViewModel.placeFull
                )
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchPlaceScreen(onSearch: (SearchState) -> Unit, toResultScreen: () -> Unit) {
    val scrollState = rememberScrollState()
    var chosen by remember { mutableIntStateOf(0) }
    var searchInput by remember { mutableStateOf("") }
    var sliderPosition by remember { mutableStateOf(0f..100f) }
    val catalogs = listOf(
        stringResource(id = R.string.culture),
        stringResource(id = R.string.leisure),
        stringResource(id = R.string.to_eat)
    )
    val picked = remember(chosen) {
        mutableStateListOf<Boolean>().apply {
            for (i in TYPES[chosen].indices) {
                add(false)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxHeight(5f / 6f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .navigationBarsPadding()
                .padding(start = 15.dp, end = 15.dp, bottom = 90.dp)
        ) {
            CatalogNavigation(
                catalogs = catalogs,
                onCatalogChange = { chosen = it },
                chosen = chosen
            )

            Spacer(modifier = Modifier.height(13.dp))

            Search(modifier = Modifier.fillMaxWidth(), onSearch = { searchInput = it })

            Spacer(modifier = Modifier.height(13.dp))

            MontsText(
                text = stringResource(id = R.string.avg_receipt),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(13.dp))
            RangeSliderWithPointers(
                range = 0f..100f,
                sliderPosition = sliderPosition,
                onValueChange = { sliderPosition = it },
                steps = 10
            )

            Spacer(modifier = Modifier.height(8.dp))

            MontsText(
                text = stringResource(id = R.string.place_type),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(13.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(17.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TYPES[chosen].forEachIndexed { i, type ->
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color = if (picked[i]) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                            .clickable(
                                indication = null,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                onClick = { picked[i] = !picked[i] }
                            )
                            .padding(horizontal = 22.dp, vertical = 11.dp)
                    ) {
                        MontsText(
                            text = stringResource(id = type),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (picked[i]) Color.White else MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            PrimaryButton(
                text = stringResource(id = R.string.next),
                paddingValues = PaddingValues(horizontal = 58.dp, vertical = 15.dp),
                onClick = {
                    onSearch(
                        SearchState(
                            input = searchInput,
                            catalog = catalogs[chosen],
                            types = mutableListOf<Int>().apply {
                                picked.forEachIndexed { index, b -> if (b) add(TYPES[chosen][index]) }
                            },
                            priceRange = sliderPosition
                        )
                    )
                    toResultScreen()
                }
            )
        }
    }
}

@Composable
fun RangeSliderWithPointers(
    range: ClosedFloatingPointRange<Float>,
    sliderPosition: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    steps: Int
) {
    var sliderWidth by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                sliderWidth = it.size.width.toFloat()
            }
    ) {

        Box {
            SliderPointer(
                sliderWidth = sliderWidth,
                valueOnSlider = sliderPosition.start,
                max = range.endInclusive,
                infoPrefix = "₽"
            )
            SliderPointer(
                sliderWidth = sliderWidth,
                valueOnSlider = sliderPosition.endInclusive,
                max = range.endInclusive,
                infoPrefix = "₽"
            )
        }
        RangeSlider(
            value = sliderPosition,
            steps = steps,
            onValueChange = onValueChange,
            valueRange = range,
            onValueChangeFinished = {}
        )
    }
}

@Composable
fun SliderPointer(sliderWidth: Float, valueOnSlider: Float, max: Float, infoPrefix: String) {
    var startSize by remember { mutableIntStateOf(0) }
    val pointRadius = 9.5f
    val density = LocalDensity.current.density
    val maxWidth = sliderWidth - pointRadius * 2 * density
    val startOffset by animateDpAsState(
        targetValue = ((valueOnSlider / max * maxWidth - startSize / 2) / density).dp,
        label = ""
    )

    Column(
        modifier = Modifier
            .offset(x = startOffset + pointRadius.dp)
            .animateContentSize()
            .onGloballyPositioned { startSize = it.size.width },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondary)
                .padding(vertical = 10.dp, horizontal = 15.dp)
        ) {
            MontsText(
                text = valueOnSlider.toInt().toString() + infoPrefix,
                fontSize = 12.sp,
                color = Color.White
            )
        }
        Icon(
            modifier = Modifier.offset(y = (-0.18).dp),
            painter = painterResource(id = R.drawable.slider_shard_pointer),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    sort: (SortState) -> Unit,
    result: List<Place>,
    removeFromFavourite: (String) -> Unit,
    addToFavourite: (String) -> Unit,
    placeFull: PlaceFull,
    getFullPlaceInfo: (String) -> Unit,
    popBack: () -> Unit
) {
    var sortState by remember { mutableStateOf(SortState()) }
    val lazyState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCardInfo by remember { mutableStateOf(false) }
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(5f / 6f)
                .padding(horizontal = 10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = popBack, modifier = Modifier.offset(x = (-16).dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = stringResource(id = R.string.back_txt),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                MontsText(
                    text = stringResource(id = R.string.by_price),
                    fontSize = 12.sp,
                    color = if (sortState.byPrice) MaterialTheme.colorScheme.primary else Color.Black,
                    modifier = Modifier.clickable {
                        sortState = sortState.copy(byPrice = !sortState.byPrice)
                        sort(sortState)
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                MontsText(
                    text = stringResource(id = R.string.by_rating),
                    fontSize = 12.sp,
                    color = if (sortState.byRating) MaterialTheme.colorScheme.primary else Color.Black,
                    modifier = Modifier.clickable {
                        sortState = sortState.copy(byRating = !sortState.byRating)
                        sort(sortState)
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                Icon(
                    painter = painterResource(id = R.drawable.sort_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Search(
                modifier = Modifier.fillMaxWidth(),
                onSearch = { sortState = sortState.copy(word = it); sort(sortState) })

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(state = lazyState, contentPadding = PaddingValues(vertical = 10.dp)) {
                items(items = result, key = Place::id) { place ->
                    val option: @Composable () -> Unit = if (place.favourite) {
                        @Composable {
                            RemoveFromFavouriteGoldCardOption(
                                onClick = { removeFromFavourite(place.id) })
                        }
                    } else {
                        @Composable { AddToFavouriteCardOption(onClick = { addToFavourite(place.id) }) }
                    }
                    DraggableCard(option1 = option) {
                        PlaceCard(
                            place = place,
                            onCardClick = {
                                getFullPlaceInfo(place.id)
                                showCardInfo = true
                            },
                            shadowColor = Color.Black.copy(alpha = 0.2f),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        if (showCardInfo) {
            val cor = rememberCoroutineScope()
            ModalBottomSheet(
                onDismissRequest = { showCardInfo = false },
                dragHandle = null,
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                PlaceInfoBottomSheet(
                    placeFull = placeFull,
                    onClose = {
                        cor.launch {
                            sheetState.hide()
                        }.invokeOnCompletion { showCardInfo = false }
                    },
                    removeFromFavourite = { removeFromFavourite(placeFull.id) },
                    addToFavourite = { addToFavourite(placeFull.id) }
                )
            }
        }
    }
}

@Composable
fun PlaceInfoBottomSheet(
    placeFull: PlaceFull,
    onClose: () -> Unit,
    removeFromFavourite: () -> Unit,
    addToFavourite: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f))

            val uriHandler = LocalUriHandler.current

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { uriHandler.openUri(placeFull.doubleGisLink) }
            ) {
                MontsText(
                    text = "2GIS",
                    fontSize = 13.sp
                )
                Icon(
                    painter = painterResource(id = R.drawable.reversed_link_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = onClose) {
                    Icon(
                        painter = painterResource(id = R.drawable.cross_gray),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }

        LazyRow(
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(items = placeFull.photos, key = { it }) {
                AsyncImage(
                    model = it,
                    contentDescription = "image",
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .width(220.dp)
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var favourite by remember { mutableStateOf(placeFull.favourite) }
            Column(modifier = Modifier.fillMaxHeight()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MontsText(
                        text = placeFull.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )
                    Icon(
                        modifier = Modifier.size(20.dp).clickable(
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
                        painter = painterResource(id = if (favourite) R.drawable.gold_bookmark else R.drawable.gray_bookmark),
                        contentDescription = "is favourite",
                        tint = Color.Unspecified
                    )

                }

                MontsText(text = placeFull.type, fontSize = 13.sp)

                Spacer(modifier = Modifier.weight(1f))

                Row {
                    MontsText(
                        text = placeFull.rating.toString(),
                        fontSize = 12.sp,
                        color = Color(0xFF1DAB4D),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    MontsText(text = placeFull.reviews.toString() + " оценок", fontSize = 12.sp)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
                val clipboardManager: ClipboardManager = LocalClipboardManager.current
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MontsText(text = placeFull.address, fontSize = 11.sp)
                    Icon(
                        painter = painterResource(id = R.drawable.copy_icon),
                        contentDescription = "copy",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.clickable {
                            clipboardManager.setText(
                                buildAnnotatedString { append(placeFull.address) })
                        })
                }
                MontsText(text = placeFull.workTime, fontSize = 11.sp)
                MontsText(text = placeFull.phone, fontSize = 11.sp)
                MontsText(text = "Средний чек " + placeFull.cost + "₽", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview
@Composable
fun CardInfoBottomSheetPreview() {
    TripNNTheme {
        Box(modifier = Modifier.background(Color.White)) {
            PlaceInfoBottomSheet(
                placeFull = PLACE_FULL_1,
                onClose = { },
                removeFromFavourite = { },
                addToFavourite = {}
            )
        }
    }
}

@Preview
@Composable
fun SearchPlaceContentPreview() {
    TripNNTheme(false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            SearchPlaceScreen(onSearch = {}, toResultScreen = {})
        }
    }
}