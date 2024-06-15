package ru.nn.tripnn.ui.screen.main.takingroute

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.R
import ru.nn.tripnn.data.datasource.stubdata.ui.PLACE_1
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.ui.common.ImageOrDefault
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

enum class CurrentPlaceButtonType {
    NEXT_PLACE, FINISH_ROUTE
}

@Composable
fun CurrentPlaceCard(
    modifier: Modifier = Modifier,
    place: Place,
    timeToWalk: Int,
    distance: Int,
    buttonType: CurrentPlaceButtonType,
    onButtonClick: (() -> Unit),
    expanded: Boolean
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .shadow(
                    color = TripNnTheme.colorScheme.shadow,
                    borderRadius = 20.dp,
                    blurRadius = 4.dp
                )
                .weight(1f)
                .height(CURRENT_PLACE_CARD_HEIGHT)
                .clip(RoundedCornerShape(20.dp))
                .background(TripNnTheme.colorScheme.background)
                .padding(horizontal = 15.dp, vertical = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    MontsText(
                        text = "$timeToWalk" + stringResource(id = R.string.minute_short),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.dot),
                        tint = TripNnTheme.colorScheme.tertiary,
                        contentDescription = ""
                    )
                    MontsText(
                        text = "$distance" + stringResource(id = R.string.metre_short),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MontsText(
                        text = place.name,
                        modifier = Modifier.fillMaxWidth(1 / 2f),
                        style = MaterialTheme.typography.labelLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                    ImageOrDefault(
                        modifier = Modifier
                            .width(70.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        imageUrl = place.photos.firstOrNull()
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        if (expanded) Spacer(modifier = Modifier.width(7.dp))

        CurrentPlaceButton(
            buttonType = buttonType,
            onButtonClick = onButtonClick,
            visible = expanded
        )
    }
}

@Composable
fun CurrentPlaceButton(
    modifier: Modifier = Modifier,
    buttonType: CurrentPlaceButtonType,
    onButtonClick: (() -> Unit),
    visible: Boolean
) {
    val color = when (buttonType) {
        CurrentPlaceButtonType.NEXT_PLACE -> TripNnTheme.colorScheme.secondary
        CurrentPlaceButtonType.FINISH_ROUTE -> TripNnTheme.colorScheme.primary
    }

    val icon = when (buttonType) {
        CurrentPlaceButtonType.NEXT_PLACE -> painterResource(id = R.drawable.next_place_icon)
        CurrentPlaceButtonType.FINISH_ROUTE -> painterResource(id = R.drawable.finish_route_icon)
    }

    val width by animateDpAsState(
        targetValue = if (visible) CURRENT_PLACE_CARD_HEIGHT else 0.dp,
        label = ""
    )

    Box(
        modifier = modifier
            .shadow(color = TripNnTheme.colorScheme.shadow, borderRadius = 20.dp, blurRadius = 4.dp)
            .clip(RoundedCornerShape(20.dp))
            .width(width)
            .height(CURRENT_PLACE_CARD_HEIGHT)
            .background(color)
            .clickable(
                enabled = visible,
                onClick = onButtonClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = icon,
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
private fun CurrentPlaceCardPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(vertical = 10.dp)
        ) {
            CurrentPlaceCard(
                modifier = Modifier.fillMaxWidth(),
                place = PLACE_1.copy(name = "Большая Покровская Улица"),
                timeToWalk = 15,
                distance = 300,
                buttonType = CurrentPlaceButtonType.NEXT_PLACE,
                onButtonClick = {},
                expanded = false
            )
        }
    }
}

@Preview
@Composable
private fun ExpendedCurrentPlaceCardPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(TripNnTheme.colorScheme.background)
                .padding(vertical = 10.dp)
        ) {
            CurrentPlaceCard(
                modifier = Modifier.fillMaxWidth(),
                place = PLACE_1.copy(name = "Большая Покровская Улица"),
                timeToWalk = 15,
                distance = 300,
                buttonType = CurrentPlaceButtonType.FINISH_ROUTE,
                onButtonClick = {},
                expanded = true
            )
        }
    }
}

@Preview
@Composable
private fun InteractiveCurrentPlaceCardPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(TripNnTheme.colorScheme.background)
                .padding(vertical = 10.dp),
        ) {
            var expanded by remember { mutableStateOf(false) }
            CurrentPlaceCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                place = PLACE_1.copy(name = "Большая Покровская Улица"),
                timeToWalk = 15,
                distance = 300,
                buttonType = CurrentPlaceButtonType.NEXT_PLACE,
                onButtonClick = { },
                expanded = expanded
            )
        }
    }
}