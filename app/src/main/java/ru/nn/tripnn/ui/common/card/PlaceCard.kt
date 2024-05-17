package ru.nn.tripnn.ui.common.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.data.datasource.stubdata.ui.PLACE_1
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun PlaceCard(
    modifier: Modifier = Modifier,
    place: Place,
    onCardClick: () -> Unit,
    shadowColor: Color = TripNnTheme.colorScheme.shadow,
    option1: @Composable () -> Unit,
    option2: @Composable (() -> Unit)? = null,
) {
    DraggableCard(modifier = modifier, option1 = option1, option2 = option2) {
        PlaceCard(
            modifier = modifier,
            place = place,
            onCardClick = onCardClick,
            shadowColor = shadowColor
        )
    }
}

@Composable
fun PlaceCard(
    modifier: Modifier = Modifier,
    place: Place,
    onCardClick: () -> Unit,
    shadowColor: Color = Color(0x00FFFFFF)
) {
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
}

@Preview
@Composable
fun PlaceCardPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(TripNnTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            PlaceCard(
                modifier = Modifier.fillMaxWidth(),
                place = PLACE_1,
                onCardClick = {  },
                shadowColor = TripNnTheme.colorScheme.shadow
            )
        }
    }
}