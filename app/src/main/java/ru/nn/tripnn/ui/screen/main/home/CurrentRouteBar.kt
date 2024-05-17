package ru.nn.tripnn.ui.screen.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.R
import ru.nn.tripnn.data.datasource.stubdata.ui.CURRENT_ROUTE
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun CurrentRouteBar(
    modifier: Modifier = Modifier,
    route: CurrentRoute?,
    onClick: () -> Unit
) {
    val percent = if (route != null && route.places.isNotEmpty()) {
        route.currentPlaceIndex * 100 / route.places.size
    } else {
        0
    }

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
                spread = (-5).dp
            )
            .clip(RoundedCornerShape(6.dp))
            .background(TripNnTheme.colorScheme.currentRoute)
            .padding(vertical = 10.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            MontsText(
                text = stringResource(id = R.string.current_route),
                style = MaterialTheme.typography.displayMedium,
                color = TripNnTheme.colorScheme.onPrimary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                MontsText(
                    text = "$percent%",
                    style = MaterialTheme.typography.labelMedium,
                    color = TripNnTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.map_icon),
                    contentDescription = stringResource(id = R.string.map_desc_icon),
                    tint = TripNnTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
fun CurrentRouteBarPreview() {
    TripNNTheme {
        CurrentRouteBar(route = CURRENT_ROUTE, onClick = {})
    }
}