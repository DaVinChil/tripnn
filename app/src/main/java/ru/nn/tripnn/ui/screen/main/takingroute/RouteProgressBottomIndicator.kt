package ru.nn.tripnn.ui.screen.main.takingroute

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.R
import ru.nn.tripnn.data.datasource.stubdata.ui.CURRENT_ROUTE
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun RouteProgressBottomIndicator(
    modifier: Modifier = Modifier,
    currentRoute: CurrentRoute
) {
    Row(
        modifier = modifier
            .shadow(color = TripNnTheme.colorScheme.shadow, borderRadius = 16.dp, blurRadius = 3.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(TripNnTheme.colorScheme.bottomSheetBackground)
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until currentRoute.places.size) {
            PlaceBottomIndicator(
                modifier = Modifier.weight(1f),
                index = i,
                currentRoute = currentRoute
            )
        }
    }
}

@Composable
fun PlaceBottomIndicator(
    modifier: Modifier = Modifier,
    index: Int,
    currentRoute: CurrentRoute
) {
    when {
        index < currentRoute.currentPlaceIndex ->
            NormalPlaceBottomIndicator(modifier, TripNnTheme.colorScheme.primary)

        currentRoute.places[index].isClosed() -> CautionPlaceBottomIndicator(modifier)

        index == currentRoute.currentPlaceIndex ->
            NormalPlaceBottomIndicator(modifier, TripNnTheme.colorScheme.secondary)

        else -> NormalPlaceBottomIndicator(modifier, TripNnTheme.colorScheme.minor)
    }
}

@Composable
fun NormalPlaceBottomIndicator(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .height(2.dp)
            .background(color)
    )
}

@Composable
fun CautionPlaceBottomIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .height(2.dp)
                .background(Color(0xFFFF6262))
        )

        Box(modifier = Modifier.size(12.dp), contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.caution_bg),
                contentDescription = "",
                tint = TripNnTheme.colorScheme.bottomSheetBackground
            )
            Icon(
                painter = painterResource(id = R.drawable.caution),
                contentDescription = "closed",
                tint = Color.Unspecified
            )
        }
    }
}

@Preview
@Composable
private fun CautionPlaceBottomIndicatorPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(TripNnTheme.colorScheme.bottomSheetBackground)
                .padding(7.dp)
        ) {
            CautionPlaceBottomIndicator(modifier = Modifier.width(40.dp))
        }
    }
}

@Preview
@Composable
private fun RouteProgressPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(50.dp)
                .background(TripNnTheme.colorScheme.background)
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            RouteProgressBottomIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                currentRoute = CURRENT_ROUTE
            )
        }
    }
}