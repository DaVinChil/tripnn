package ru.nn.tripnn.ui.screen.main.search

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.SearchFilters
import ru.nn.tripnn.ui.common.CatalogNavigation
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme
import ru.nn.tripnn.ui.theme.montserratFamily
import java.time.LocalTime

@Composable
fun SearchPlaceBuilderScreen(
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

            FilterByWord(text = searchInput, onChange = { searchInput = it })

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterByWord(text: String, onChange: (String) -> Unit) {
    Column {
        MontsText(
            text = stringResource(id = R.string.filter_by_word),
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(13.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(TripNnTheme.colorScheme.fieldBackground)
        ) {
            BasicTextField(
                value = text,
                onValueChange = {
                    if (it.length <= 20) {
                        onChange(it)
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        this.defaultKeyboardAction(ImeAction.Done)
                    }
                ),
                singleLine = true,
                textStyle = TextStyle(
                    color = TripNnTheme.colorScheme.textColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = montserratFamily,
                    textAlign = TextAlign.Start
                )
            ) { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = text,
                    visualTransformation = VisualTransformation.None,
                    innerTextField = innerTextField,
                    singleLine = true,
                    enabled = true,
                    placeholder = {
                        MontsText(
                            text = stringResource(id = R.string.search),
                            style = MaterialTheme.typography.labelMedium,
                            color = TripNnTheme.colorScheme.hint,
                        )
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    contentPadding = PaddingValues(
                        horizontal = 15.dp,
                        vertical = 7.dp
                    ),
                    shape = RoundedCornerShape(5.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = TripNnTheme.colorScheme.fieldBackground,
                        unfocusedContainerColor = TripNnTheme.colorScheme.fieldBackground,
                        focusedPlaceholderColor = TripNnTheme.colorScheme.hint,
                        unfocusedPlaceholderColor = TripNnTheme.colorScheme.hint
                    )
                )
            }
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

@Preview
@Composable
fun SearchPlaceContentPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TripNnTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            SearchPlaceBuilderScreen(onSearch = {}, toResultScreen = {})
        }
    }
}