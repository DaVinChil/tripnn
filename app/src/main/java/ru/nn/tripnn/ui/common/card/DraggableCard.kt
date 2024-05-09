package ru.nn.tripnn.ui.common.card

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.R
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme
import kotlin.math.roundToInt

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

    val anchors by remember(endOfDragging) {
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
        dragState.updateAnchors(anchors)
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
    color: Color = TripNnTheme.colorScheme.textColor
) {
    Column(
        modifier = Modifier
            .height(CARD_HEIGHT)
            .fillMaxWidth(1f / 4f)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = painter),
            contentDescription = "option",
            tint = color
        )
        MontsText(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = color
        )
    }
}

@Composable
fun AddToFavouriteCardOption(onClick: () -> Unit) {
    CardOption(
        painter = R.drawable.unselected_bookmark,
        text = stringResource(id = R.string.add_to_favourite),
        onClick = onClick
    )
}

@Composable
fun RemoveFromFavouriteGoldCardOption(onClick: () -> Unit) {
    CardOption(
        painter = R.drawable.gold_bookmark,
        text = stringResource(id = R.string.remove_from_favourite),
        onClick = onClick
    )
}

@Composable
fun RemoveFromFavouriteRedCardOption(onClick: () -> Unit) {
    CardOption(
        painter = R.drawable.unselected_bookmark,
        text = stringResource(id = R.string.remove_from_favourite),
        color = Color.Red,
        onClick = onClick
    )
}

@Composable
fun RemoveFromRouteCardOption(onClick: () -> Unit) {
    CardOption(
        painter = R.drawable.trashcan_outlined,
        text = stringResource(id = R.string.remove_from_route),
        onClick = onClick,
        color = Color.Red
    )
}

@Composable
fun ReplaceCardOption(onClick: () -> Unit) {
    CardOption(
        painter = R.drawable.change_icon,
        text = stringResource(id = R.string.replace_place),
        onClick = onClick
    )
}

@Preview
@Composable
fun DraggablePlaceCardPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(TripNnTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            PlaceCard(
                place = PLACE_1,
                onCardClick = { },
                shadowColor = TripNnTheme.colorScheme.shadow,
                option1 = { AddToFavouriteCardOption(onClick = {}) },
                option2 = { RemoveFromRouteCardOption(onClick = {}) },
            )
        }
    }
}