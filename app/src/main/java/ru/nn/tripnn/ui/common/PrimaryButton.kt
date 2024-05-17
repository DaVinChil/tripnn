package ru.nn.tripnn.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color = TripNnTheme.colorScheme.primary,
    textColor: Color = TripNnTheme.colorScheme.onPrimary,
    paddingValues: PaddingValues = PaddingValues(vertical = 17.dp, horizontal = 25.dp),
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    fontSize: TextUnit = 16.sp
) {
    Box(
        modifier = Modifier
            .shadow(borderRadius = 100.dp, blurRadius = 10.dp, spread = (-5).dp)
            .clip(RoundedCornerShape(100))
            .then(modifier)
            .wrapContentSize()
    ) {
        Box(
            modifier = modifier
                .rippleClickable(enabled = enabled, onClick = onClick)
                .background(containerColor)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            MontsText(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = fontSize,
                color = textColor,
                maxLines = 1
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.White.copy(alpha = 0.3f))
                    .pointerInput(false) {}
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(35.dp)
                        .align(Alignment.Center),
                    color = TripNnTheme.colorScheme.minor,
                    trackColor = TripNnTheme.colorScheme.tertiary,
                )
            }
        }
    }

}

@Preview
@Composable
fun PrimaryButtonPreview() {
    TripNNTheme {
        Surface(
            modifier = Modifier
                .background(TripNnTheme.colorScheme.background)
                .size(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                PrimaryButton(text = stringResource(id = R.string.next))
            }
        }
    }
}

@Preview
@Composable
fun PrimaryButtonLoadingPreview() {
    TripNNTheme {
        Surface(
            modifier = Modifier
                .background(TripNnTheme.colorScheme.background)
                .size(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                PrimaryButton(text = stringResource(id = R.string.next), isLoading = true)
            }
        }
    }
}