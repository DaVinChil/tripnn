package ru.nn.tripnn.ui.screen.main.takingroute

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.data.database.usersettings.Theme
import ru.nn.tripnn.data.datasource.stubdata.ui.CURRENT_ROUTE
import ru.nn.tripnn.data.datasource.stubdata.ui.PLACE_1
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.state.ResState
import ru.nn.tripnn.ui.common.DragHandle
import ru.nn.tripnn.ui.common.InternetProblemScreen
import ru.nn.tripnn.ui.common.LoadingCircleScreen
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.screen.main.takingroute.map.CurrentRouteMap
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

val CURRENT_PLACE_CARD_HEIGHT = 100.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakingTheRouteScreen(
    currentRoute: ResState<CurrentRoute?>,
    onBack: () -> Unit,
    onNextPlaceClick: () -> Unit,
    addCurrentRouteToFavourite: () -> Unit,
    removeCurrentRouteFromFavourite: () -> Unit,
    addPlaceToFavourite: (Place) -> Unit,
    removePlaceFromFavourite: (Place) -> Unit,
    removePlaceFromRoute: (Int) -> Unit,
    toPlacePhotos: (String, Int) -> Unit
) {
    val currentRouteValue = currentRoute.getOrNull()

    if (currentRoute.isError()) {
        InternetProblemScreen()
        return
    }

    if (currentRoute.isLoading() || currentRouteValue == null) {
        LoadingCircleScreen()
        return
    }

    var expended by remember { mutableStateOf(false) }
    val sheetState = rememberStandardBottomSheetState(confirmValueChange = { state ->
        expended = state == SheetValue.Expanded
        true
    })
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

    Box(modifier = Modifier.fillMaxSize()) {
        if (currentRouteValue.finished) {
            FinishScreen(
                modifier = Modifier.align(Alignment.BottomCenter),
                toMainScreen = onBack,
                addToFavourite = addCurrentRouteToFavourite,
                removeFromFavourite = removeCurrentRouteFromFavourite,
                currentRoute = currentRouteValue
            )
        } else {
            BottomSheetScaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState,
                sheetContent = {
                    CurrentRouteBottomSheetContent(
                        currentRoute = currentRouteValue,
                        expanded = expended,
                        onNextPlaceClick = onNextPlaceClick,
                        addCurrentRouteToFavourite = addCurrentRouteToFavourite,
                        addPlaceToFavourite = addPlaceToFavourite,
                        removePlaceFromFavourite = removePlaceFromFavourite,
                        removePlaceFromRoute = removePlaceFromRoute,
                        toPlacePhotos = toPlacePhotos,
                        removeCurrentRouteFromFavourite = removeCurrentRouteFromFavourite
                    )
                },
                sheetTonalElevation = 0.dp,
                sheetShadowElevation = 0.dp,
                sheetPeekHeight = CURRENT_PLACE_CARD_HEIGHT + 110.dp,
                sheetContainerColor = Color.Transparent,
                sheetDragHandle = { DragHandle() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(TripNnTheme.colorScheme.undefined)
                ) {
                    CurrentRouteMap(currentRoute = currentRouteValue)

                    Box(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(10.dp)
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .shadow(
                                    color = TripNnTheme.colorScheme.shadow,
                                    borderRadius = 100.dp,
                                    blurRadius = 2.dp
                                )
                                .clip(RoundedCornerShape(100))
                                .background(TripNnTheme.colorScheme.background)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_arrow),
                                contentDescription = "",
                                tint = TripNnTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }

            RouteProgressBottomIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                currentRoute = currentRouteValue
            )
        }
    }
}

@Composable
fun CurrentRouteBottomSheetContent(
    modifier: Modifier = Modifier,
    currentRoute: CurrentRoute,
    expanded: Boolean,
    onNextPlaceClick: () -> Unit,
    addCurrentRouteToFavourite: () -> Unit,
    removeCurrentRouteFromFavourite: () -> Unit,
    addPlaceToFavourite: (Place) -> Unit,
    removePlaceFromFavourite: (Place) -> Unit,
    removePlaceFromRoute: (Int) -> Unit,
    toPlacePhotos: (String, Int) -> Unit
) {
    val curPlaceInd = currentRoute.currentPlaceIndex

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CurrentPlaceCard(
            modifier = Modifier.fillMaxWidth(),
            place = currentRoute.places[curPlaceInd],
            timeToWalk = currentRoute.walkInfo.getOrNull(curPlaceInd)?.timeToWalk ?: 0,
            distance = currentRoute.walkInfo.getOrNull(curPlaceInd)?.distance ?: 0,
            buttonType = if (currentRoute.currentPlaceIndex != currentRoute.places.lastIndex) {
                CurrentPlaceButtonType.NEXT_PLACE
            } else {
                CurrentPlaceButtonType.FINISH_ROUTE
            },
            onButtonClick = onNextPlaceClick,
            expanded = expanded
        )

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(TripNnTheme.colorScheme.bottomSheetBackground)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            CurrentRouteDropdownMenu(
                modifier = Modifier.align(Alignment.End),
                currentRoute = currentRoute,
                addCurrentRouteToFavourite = addCurrentRouteToFavourite,
                removeCurrentRouteFromFavourite = removeCurrentRouteFromFavourite
            )

            CurrentRoutePlaceColumn(
                modifier = Modifier.fillMaxWidth(),
                currentRoute = currentRoute,
                addPlaceToFavourite = addPlaceToFavourite,
                removePlaceFromFavourite = removePlaceFromFavourite,
                removePlaceFromRoute = removePlaceFromRoute,
                toPlacePhotos = toPlacePhotos
            )
        }
    }
}

@Composable
fun CurrentRouteDropdownMenu(
    modifier: Modifier = Modifier,
    currentRoute: CurrentRoute,
    addCurrentRouteToFavourite: () -> Unit,
    removeCurrentRouteFromFavourite: () -> Unit
) {
    Box(modifier = modifier) {
        var showDropdownMenu by remember { mutableStateOf(false) }

        IconButton(onClick = { showDropdownMenu = true }) {
            Icon(
                painter = painterResource(id = R.drawable.vertical_kabab_menu),
                contentDescription = "menu",
                tint = TripNnTheme.colorScheme.onMinor
            )

            DropdownMenu(
                expanded = showDropdownMenu,
                onDismissRequest = { showDropdownMenu = false },
                borderRadius = 8.dp,
                containerColor = TripNnTheme.colorScheme.bottomSheetBackground
            ) {
                DropdownMenuItem(
                    text = {
                        val text = if (currentRoute.favourite) {
                            stringResource(id = R.string.add_route_to_favourite)
                        } else {
                            stringResource(id = R.string.remove_route_from_favourite)
                        }

                        MontsText(
                            text = text,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(end = 10.dp),
                            color = TripNnTheme.colorScheme.textColor
                        )
                    },
                    trailingIcon = {
                        val painter = if (currentRoute.favourite) {
                            painterResource(id = R.drawable.gold_bookmark)
                        } else {
                            painterResource(id = R.drawable.unselected_bookmark)
                        }

                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painter,
                            contentDescription = "",
                            tint = if (currentRoute.favourite) Color.Unspecified
                            else TripNnTheme.colorScheme.tertiary
                        )
                    },
                    onClick = if (currentRoute.favourite) {
                        addCurrentRouteToFavourite
                    } else removeCurrentRouteFromFavourite,
                )

                HorizontalDivider(color = TripNnTheme.colorScheme.minor)

                DropdownMenuItem(
                    text = {
                        MontsText(
                            text = stringResource(id = R.string.finish_the_route),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(end = 10.dp),
                            color = Color(0xFFFF6262)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.caution),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )
                    },
                    onClick = addCurrentRouteToFavourite,
                )
            }
        }
    }
}

@Preview
@Composable
private fun TakingTheRouteScreenPreview() {
    TripNNTheme(theme = Theme.LIGHT) {
        TakingTheRouteScreen(
            currentRoute = ResState.Success(CURRENT_ROUTE),
            onBack = { },
            onNextPlaceClick = { },
            addCurrentRouteToFavourite = { },
            removeCurrentRouteFromFavourite = { },
            addPlaceToFavourite = {},
            removePlaceFromFavourite = {},
            removePlaceFromRoute = { },
            toPlacePhotos = { _, _ -> }
        )
    }
}