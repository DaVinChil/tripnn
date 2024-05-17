package ru.nn.tripnn.ui.screen.main.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.rippleClickable
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun NewRouteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    hasDraft: Boolean
) {
    val buttonInteractionSource = remember { MutableInteractionSource() }
    val pressed by buttonInteractionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (pressed) 0.85f else 1f, label = "")

    val height = 140.dp
    val lineHeight = 30.sp
    val yOffset = (-9).dp

    Box(
        modifier = modifier
            .scale(scale)
            .height(height)
            .width(230.dp)
            .rippleClickable(
                onClick = onClick,
                indication = null,
                interactionSource = buttonInteractionSource
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(height)
                .shadow(
                    borderRadius = 100.dp,
                    blurRadius = 20.dp,
                    color = TripNnTheme.colorScheme.newRouteGlow
                )
                .clip(RoundedCornerShape(100))
                .background(TripNnTheme.colorScheme.secondary)
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.new_route),
                style = MaterialTheme.typography.titleLarge,
                color = TripNnTheme.colorScheme.textColor,
                textAlign = TextAlign.Center,
                lineHeight = lineHeight,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.width(IntrinsicSize.Min)
            )

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopCenter) {
                MontsText(
                    text = if (hasDraft) stringResource(id = R.string.draft) else stringResource(id = R.string.create),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.offset(y = yOffset)
                )
            }
        }
    }
}

@Preview
@Composable
fun NewRouteButtonPreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .background(TripNnTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            NewRouteButton(onClick = {}, hasDraft = false)
        }
    }
}