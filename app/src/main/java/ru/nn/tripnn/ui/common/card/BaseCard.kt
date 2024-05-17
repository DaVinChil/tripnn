package ru.nn.tripnn.ui.common.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.common.ImageOrDefault
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.rippleClickable
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

val CARD_HEIGHT = 140.dp
val CARD_WIDTH = 340.dp

enum class DragValue { Start, End }

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    type: String?,
    name: String,
    visited: Boolean? = null,
    closed: Boolean? = null,
    hideIndication: Boolean = false,
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
            .background(TripNnTheme.colorScheme.cardBackground)
            .rippleClickable(onClick = onCardClick)
    ) {
        Row {
            ImageWithIndications(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.5f),
                imageUrl = imageUrl,
                closed = closed,
                visited = visited,
                hideIndication = hideIndication
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                MontsText(text = type ?: "")
                MontsText(text = name, style = MaterialTheme.typography.labelLarge, maxLines = 2)
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
fun ImageWithIndications(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    closed: Boolean?,
    visited: Boolean?,
    hideIndication: Boolean
) {
    Box(modifier = modifier) {
        ImageOrDefault(modifier = Modifier.fillMaxSize(), imageUrl = imageUrl)

        if (closed == true && !hideIndication) {
            ClosedIndication(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        } else if (visited == true && !hideIndication) {
            VisitedIndication(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun VisitedIndication(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.7f),
                        Color.Black.copy(alpha = 0.9f)
                    )
                )
            )
            .padding(horizontal = 7.dp, vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.flag),
                contentDescription = "flag",
                tint = Color.Unspecified,
                modifier = Modifier.size(15.dp)
            )
            MontsText(
                text = stringResource(id = R.string.already_visited),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White 
            )
        }
    }
}

@Composable
fun ClosedIndication(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.7f),
                        Color.Black.copy(alpha = 0.9f)
                    )
                )
            )
            .padding(horizontal = 7.dp, vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.caution),
                contentDescription = "caution",
                tint = Color.Unspecified,
                modifier = Modifier.size(15.dp)
            )

            MontsText(
                text = stringResource(id = R.string.closed),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
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
            contentDescription = "Cost",
            tint = TripNnTheme.colorScheme.tertiary
        )
        MontsText(
            text = "$cost ₽",
            style = MaterialTheme.typography.displaySmall
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
            contentDescription = "Rating",
            tint = TripNnTheme.colorScheme.tertiary
        )
        MontsText(
            text = rating.toString(),
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@Composable
fun LoadingCard(
    modifier: Modifier = Modifier,
    shadowColor: Color = TripNnTheme.colorScheme.shadow
) {
    val shimmerInstance =
        rememberShimmer(shimmerBounds = ShimmerBounds.Window, theme = TripNnTheme.shimmer)

    Box(
        modifier = modifier
            .shadow(
                color = shadowColor,
                borderRadius = 10.dp,
                blurRadius = 10.dp
            )
            .clip(RoundedCornerShape(10.dp))
            .height(CARD_HEIGHT)
            .background(TripNnTheme.colorScheme.cardBackground)
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
                        .background(TripNnTheme.colorScheme.undefined)
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
                            .background(TripNnTheme.colorScheme.undefined)
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
                            .background(TripNnTheme.colorScheme.undefined)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoadingCardPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(TripNnTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            LoadingCard()
        }
    }
}

@Preview
@Composable
fun ClosedCardPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(TripNnTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            BaseCard(
                imageUrl = "",
                visited = false,
                name = "Name",
                closed = true,
                onCardClick = {},
                type = "Type",
                shadowColor = TripNnTheme.colorScheme.shadow
            )
        }
    }
}

@Preview
@Composable
fun VisitedCardPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(TripNnTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            BaseCard(
                imageUrl = "",
                visited = true,
                name = "Name",
                closed = false,
                onCardClick = {},
                type = "Type",
                shadowColor = TripNnTheme.colorScheme.shadow
            )
        }
    }
}