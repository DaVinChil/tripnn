package ru.nn.tripnn.ui.screen.application.search

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.common.CatalogNavigation
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PrimaryButton
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

data class SearchState(
    val input: String,
    val types: List<Int>,
    val catalog: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPlaceBottomSheet(onDismissRequest: () -> Unit, onSearch: (SearchState) -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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
                SearchPlaceContent(onSearch = onSearch, toResultScreen = {
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

            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchPlaceContent(onSearch: (SearchState) -> Unit, toResultScreen: () -> Unit) {
    val scrollState = rememberScrollState()
    var chosen by remember { mutableIntStateOf(0) }
    var searchInput by remember { mutableStateOf("") }
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
                .padding(start = 10.dp, end = 10.dp, bottom = 90.dp)
        ) {
            CatalogNavigation(
                catalogs = catalogs,
                onCatalogChange = { chosen = it },
                chosen = chosen
            )

            Spacer(modifier = Modifier.height(13.dp))

            Search(modifier = Modifier.fillMaxWidth(), onSearch = { searchInput = it })

            Spacer(modifier = Modifier.height(13.dp))

            MontsText(text = stringResource(id = R.string.avg_receipt), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(13.dp))
            RangeSliderWithPointers(0f..100f, steps = 10)

            Spacer(modifier = Modifier.height(8.dp))

            MontsText(text = stringResource(id = R.string.place_type), fontSize = 16.sp, fontWeight = FontWeight.Medium)

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
                            }
                        )
                    )
                    toResultScreen()
                }
            )
        }
    }
}

@Composable
fun RangeSliderWithPointers(range: ClosedFloatingPointRange<Float>, steps: Int) {
    var sliderWidth by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                sliderWidth = it.size.width.toFloat()
            }
    ) {
        val max = range.endInclusive
        var sliderPosition by remember { mutableStateOf(range) }

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
            onValueChange = { range -> sliderPosition = range },
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
            SearchPlaceContent(onSearch = {}, toResultScreen = {})
        }
    }
}