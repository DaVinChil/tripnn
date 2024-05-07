package ru.nn.tripnn.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.util.darkShimmer
import kotlin.math.roundToInt

val CARD_HEIGHT = 140.dp
val CARD_WIDTH = 340.dp

enum class DragValue { Start, End }

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    type: String?,
    name: String,
    onCardClick: () -> Unit,
    shadowColor: Color = Color(0x00FFFFFF),
    info: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier
            .shadow(
                color = shadowColor,
                borderRadius = 10.dp,
                blurRadius = 10.dp
            )
            .clip(RoundedCornerShape(10.dp))
            .height(CARD_HEIGHT)
            .background(Color.White)
            .clickable(onClick = onCardClick)
    ) {
        Row {
            AsyncImage(
                model = imageUrl,
                contentDescription = "image",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .fillMaxWidth(0.5f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                MontsText(text = type ?: "", fontSize = 10.sp)
                MontsText(text = name, fontSize = 16.sp, maxLines = 2)
                if (info != null) {
                    Box(
                        contentAlignment = Alignment.BottomStart
                    ) {
                        info()
                    }
                } else {
                    Spacer(modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun LoadingCard(
    modifier: Modifier = Modifier,
    shadowColor: Color = Color(0x00FFFFFF)
) {
    val shimmerInstance =
        rememberShimmer(shimmerBounds = ShimmerBounds.Window, theme = darkShimmer)

    Box(
        modifier = modifier
            .shadow(
                color = shadowColor,
                borderRadius = 10.dp,
                blurRadius = 10.dp
            )
            .clip(RoundedCornerShape(10.dp))
            .height(CARD_HEIGHT)
            .background(Color.White)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()
                    .shimmer(shimmerInstance)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondary)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .height(17.dp)
                        .width(60.dp)
                        .shimmer(shimmerInstance)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .height(22.dp)
                        .width(99.dp)
                        .shimmer(shimmerInstance)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceCard(
    modifier: Modifier = Modifier,
    place: Place,
    onCardClick: () -> Unit,
    shadowColor: Color = Color(0x00FFFFFF),
    isLoading: Boolean = false
) {
    if (!isLoading) {
        BaseCard(
            modifier = modifier,
            imageUrl = place.photos[0],
            type = place.type,
            name = place.name,
            onCardClick = onCardClick,
            shadowColor = shadowColor
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                RatingInfo(rating = place.rating)
                if (place.cost != null) {
                    CostInfo(cost = place.cost)
                }
            }
        }
    } else {
        LoadingCard(modifier, shadowColor)
    }
}

@Composable
fun RouteCard(
    modifier: Modifier = Modifier,
    route: Route,
    onCardClick: () -> Unit,
    shadowColor: Color = Color(0x00FFFFFF)
) {
    BaseCard(
        modifier = modifier,
        imageUrl = route.imageUrl,
        name = route.name,
        type = stringResource(id = R.string.route),
        onCardClick = onCardClick,
        shadowColor = shadowColor
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            if (route.cost != null) {
                CostInfo(cost = route.cost)
            }
            if (route.rating != null) {
                RatingInfo(rating = route.rating)
            }
        }
    }
}

@Composable
fun CostInfo(cost: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Icon(
            modifier = Modifier.size(15.dp),
            painter = painterResource(id = R.drawable.wallet),
            contentDescription = "Cost"
        )
        MontsText(
            text = "$cost ₽",
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RatingInfo(rating: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Icon(
            modifier = Modifier.size(15.dp),
            painter = painterResource(id = R.drawable.outlined_gray_star),
            contentDescription = "Rating"
        )
        MontsText(
            text = rating.toString(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableCard(
    modifier: Modifier = Modifier,
    option1: @Composable () -> Unit,
    option2: (@Composable () -> Unit)? = null,
    card: @Composable () -> Unit
) {
    val density = LocalDensity.current
    var maxWidth by remember { mutableFloatStateOf(0f) }

    val endOfDragging = if (option2 != null) {
        -(maxWidth / 2)
    } else {
        -(maxWidth / 4)
    }

    val anchores by remember(endOfDragging) {
        mutableStateOf(
            DraggableAnchors {
                DragValue.Start at 0f
                DragValue.End at endOfDragging
            }
        )
    }
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Start,
            positionalThreshold = { it * 0.3f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(200)
        )
    }

    SideEffect {
        dragState.updateAnchors(anchores)
    }

    Box(
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            option1()
            option2?.let { it() }
        }
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        y = 0,
                        x = dragState
                            .requireOffset()
                            .roundToInt()
                    )
                }
                .anchoredDraggable(dragState, Orientation.Horizontal)
                .onGloballyPositioned { maxWidth = it.size.width.toFloat() }
        ) {
            card()
        }
    }

}

@Composable
fun CardOption(
    @DrawableRes painter: Int,
    text: String,
    onClick: () -> Unit,
    color: Color = Color.Unspecified
) {
    Column(
        modifier = Modifier
            .height(CARD_HEIGHT)
            .fillMaxWidth(1f / 4f)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = painter),
            contentDescription = "",
            tint = color
        )
        MontsText(text = text, fontSize = 10.sp, textAlign = TextAlign.Center, color = color)
    }
}

@Composable
fun AddToFavouriteCardOption(onClick: () -> Unit) {
    CardOption(
        painter = R.drawable.unselected_bookmark,
        text = "Добавить в избранные",
        onClick = onClick
    )
}

@Composable
fun RemoveFromFavouriteGoldCardOption(onClick: () -> Unit) {
    CardOption(painter = R.drawable.gold_bookmark, text = "Убрать из избранных", onClick = onClick)
}

@Composable
fun RemoveFromFavouriteRedCardOption(onClick: () -> Unit) {
    CardOption(
        painter = R.drawable.unselected_bookmark,
        text = "Убрать из избранных",
        color = Color.Red,
        onClick = onClick
    )
}

@Composable
fun RemoveFromRouteCardOption(onClick: () -> Unit) {
    CardOption(
        painter = R.drawable.trashcan_outlined,
        text = "Удалить из маршрута",
        onClick = onClick,
        color = Color.Red
    )
}

@Composable
fun ReplaceCardOption(onClick: () -> Unit) {
    CardOption(
        painter = R.drawable.change_icon,
        text = "Заменить место",
        onClick = onClick
    )
}

@Preview
@Composable
fun PlaceCardPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp)
        ) {
            PlaceCard(
                modifier = Modifier.fillMaxWidth(),
                place = PLACE_1,
                onCardClick = { /*TODO*/ },
                shadowColor = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun DraggablePlaceCardPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp)
        ) {
            DraggableCard(
                option1 = { AddToFavouriteCardOption(onClick = {}) },
                option2 = { RemoveFromRouteCardOption(onClick = {}) },
                card = {
                    PlaceCard(
                        place = PLACE_1,
                        onCardClick = { /*TODO*/ },
                        shadowColor = Color.Black
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun LoadingCardPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp)
        ) {
            LoadingCard(shadowColor = Color.Black)
        }
    }
}