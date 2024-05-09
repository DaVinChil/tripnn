package ru.nn.tripnn.ui.common.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.Route

@Composable
fun RouteCard(
    modifier: Modifier = Modifier,
    route: Route,
    onCardClick: () -> Unit,
    shadowColor: Color = Color(0x00FFFFFF),
    option1: @Composable () -> Unit,
    option2: @Composable (() -> Unit)? = null
) {
    DraggableCard(modifier = modifier, option1 = option1, option2 = option2) {
        RouteCard(
            modifier = modifier,
            route = route,
            onCardClick = onCardClick,
            shadowColor = shadowColor
        )
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