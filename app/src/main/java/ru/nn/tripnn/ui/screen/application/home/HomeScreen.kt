package ru.nn.tripnn.ui.screen.application.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.ROUTES
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.screen.HomeScreenData
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.RouteCard
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.screen.authentication.SystemBarsToBackgroundColor
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.montserratFamily

@Composable
fun HomeScreen(
    homeScreenState: HomeScreenState,
    onAccountClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAllRoutesClick: () -> Unit,
    onRouteCardClick: (Route) -> Unit,
    onAllPlacesClick: () -> Unit,
    onNewRouteClick: () -> Unit,
    onCurRouteClick: (() -> Unit)? = null
) {
    val screenData = homeScreenState.homeScreenData

    SystemBarsToBackgroundColor(
        statusColor = MaterialTheme.colorScheme.onSurface,
        navColor = Color.White
    )

    if (!homeScreenState.isLoading && screenData != null) {
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerContent = {
                Menu(
                    onAccountClick = onAccountClick,
                    onHistoryClick = onHistoryClick,
                    onFavouriteClick = onFavouriteClick,
                    onSettingsClick = onSettingsClick,
                    onClose = { scope.launch { drawerState.close() } }
                )
            },
            drawerState = drawerState
        ) {
            HomeContent(
                curRoutePercent = screenData.curRoutePercent,
                recRoutes = screenData.recommendedRoutes,
                onCurRouteClick = onCurRouteClick,
                onAllRoutesClick = onAllRoutesClick,
                onRouteCardClick = onRouteCardClick,
                onAllPlacesClick = onAllPlacesClick,
                onNewRouteClick = onNewRouteClick,
                onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }


}

@Composable
fun HomeContent(
    recRoutes: List<Route>,
    onAllRoutesClick: () -> Unit,
    onRouteCardClick: (Route) -> Unit,
    onAllPlacesClick: () -> Unit,
    onNewRouteClick: () -> Unit,
    curRoutePercent: Int = 0,
    onCurRouteClick: (() -> Unit)? = null,
    onMenuClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onSurface)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            Icon(
                                modifier = Modifier
                                    .clickable(onClick = onMenuClick),
                                painter = painterResource(id = R.drawable.burger_menu),
                                contentDescription = "menu",
                            )
                        }
                        Image(
                            modifier = Modifier,
                            painter = painterResource(id = R.drawable.tripnn_logo),
                            contentDescription = "log"
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MontsText(
                            text = "Рекомендованные маршруты",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        MontsText(
                            modifier = Modifier.clickable(
                                onClick = onAllRoutesClick
                            ),
                            text = "Все",
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyRow(
                        modifier = Modifier.requiredWidth(LocalConfiguration.current.screenWidthDp.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(items = recRoutes, key = Route::id) {
                            RouteCard(
                                route = it,
                                onCardClick = { onRouteCardClick(it) },
                                shadowColor = Color.Black.copy(alpha = 0.2f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    AllPlacesButton(onClick = onAllPlacesClick)

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    NewRouteButton(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = onNewRouteClick
                    )
                }
                if (onCurRouteClick != null) {
                    CurrentRouteBar(percent = curRoutePercent, onClick = onCurRouteClick)
                }
            }
        }
    }
}

@Composable
fun Menu(
    onAccountClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.8f)
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column() {
            IconButton(
                onClick = onClose,
                modifier = Modifier.offset(x = (-14).dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cross_menu),
                    contentDescription = "close menu",
                    modifier = Modifier
                        .size(16.dp)
                )
            }

            MenuOption(onClick = onAccountClick, text = "Аккаунт")
            MenuOption(onClick = onHistoryClick, text = "История маршрутов")
            MenuOption(onClick = onFavouriteClick, text = "Избранные")
            MenuOption(onClick = onSettingsClick, text = "Настройки")
        }
    }
}

@Composable
fun MenuOption(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .height(80.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        MontsText(
            modifier = Modifier,
            text = text,
            fontSize = 18.sp,
        )
    }
}

@Composable
fun AllPlacesButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(onClick = onClick)
            .shadow(borderRadius = 6.dp, blurRadius = 10.dp, spread = (-5).dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .padding(vertical = 10.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.list_icon),
                contentDescription = "all places"
            )
            MontsText(
                text = "Все места Нижнего Новгорода",
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
fun NewRouteButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    var isFillMaxWidth by remember { mutableStateOf(true) }

    val height by animateDpAsState(targetValue = if (isFillMaxWidth) 140.dp else 120.dp, label = "")
    val nrTextSize by animateIntAsState(targetValue = if (isFillMaxWidth) 40 else 30, label = "")
    val createTextSize by animateIntAsState(
        targetValue = if (isFillMaxWidth) 12 else 9,
        label = ""
    )
    val lineHeight by animateIntAsState(targetValue = if (isFillMaxWidth) 30 else 25, label = "")
    val yOffset by animateDpAsState(
        targetValue = if (isFillMaxWidth) (-9).dp else (-5).dp,
        label = ""
    )

    Box(
        modifier = modifier
            .height(height)
            .width(230.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                isFillMaxWidth = it !is PressInteraction.Press
                            }
                        }
                    }),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(height)
                .shadow(
                    borderRadius = 100.dp,
                    blurRadius = 20.dp,
                    color = Color.Black.copy(alpha = 0.3f)
                )
                .clip(RoundedCornerShape(100))
                .background(MaterialTheme.colorScheme.onSurface)
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "НОВЫЙ МАРШРУТ",
                fontFamily = montserratFamily,
                fontSize = nrTextSize.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center,
                lineHeight = lineHeight.sp,
                letterSpacing = (-0.5).sp
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopCenter) {
                MontsText(
                    text = "создать",
                    fontSize = createTextSize.sp,
                    modifier = Modifier.offset(y = yOffset)
                )
            }
        }

    }
}

@Composable
fun CurrentRouteBar(modifier: Modifier = Modifier, percent: Int, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
            .shadow(
                borderRadius = 6.dp,
                blurRadius = 10.dp,
                spread = (-5).dp,
                color = Color.Black.copy(alpha = 0.3f)
            )
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(vertical = 10.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            MontsText(
                text = "Текущий маршрут",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                MontsText(text = "$percent%", fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.map_icon),
                    contentDescription = "icon",
                    tint = Color.White
                )
            }

        }
    }
}

@Preview
@Composable
fun CurrentRouteBarPreview() {
    TripNNTheme {
        CurrentRouteBar(percent = 13, onClick = {})
    }
}

@Preview
@Composable
fun NewRouteButtonPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp)
        ) {
            NewRouteButton(onClick = {})
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    TripNNTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                HomeScreen(
                    homeScreenState = HomeScreenState(
                        isLoading = false,
                        homeScreenData = HomeScreenData(
                            recommendedRoutes = ROUTES,
                            curRoutePercent = 33
                        )
                    ),
                    onAllRoutesClick = {},
                    onRouteCardClick = {},
                    onAllPlacesClick = {},
                    onCurRouteClick = {},
                    onNewRouteClick = {},
                    onAccountClick = {},
                    onFavouriteClick = {},
                    onSettingsClick = {},
                    onHistoryClick = {}
                )
            }
        }
    }
}