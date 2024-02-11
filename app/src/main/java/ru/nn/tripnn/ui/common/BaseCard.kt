package ru.nn.tripnn.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.ui.theme.TripNNTheme

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    type: String,
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
            .height(140.dp)
            .width(340.dp)
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

            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                MontsText(text = type, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(28.dp))
                MontsText(text = name, fontSize = 16.sp)

                if (info != null) {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        info()
                    }
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
    shadowColor: Color = Color(0x00FFFFFF)
) {
    BaseCard(
        modifier = modifier,
        imageUrl = place.imageUrl,
        type = place.type,
        name = place.name,
        onCardClick = onCardClick,
        shadowColor = shadowColor
    ) {
        Column {
            RatingInfo(rating = place.rating)
            Spacer(modifier = Modifier.height(5.dp))
            CostInfo(cost = place.cost)
        }
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
        type = "Маршрут",
        onCardClick = onCardClick,
        shadowColor = shadowColor
    ) {
        Column {
            CostInfo(cost = route.cost)
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
