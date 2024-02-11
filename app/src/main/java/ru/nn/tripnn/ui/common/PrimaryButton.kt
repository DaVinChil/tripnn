package ru.nn.tripnn.ui.common

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.montserratFamily

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = Color.White,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .shadow(borderRadius = 100.dp, blurRadius = 10.dp, spread = (-5).dp)
            .clip(RoundedCornerShape(100))
            .clickable(onClick = onClick)
            .background(containerColor)
            .padding(vertical = 15.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = textColor
        )
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    TripNNTheme {
        Surface(
            modifier = Modifier
                .background(Color.White)
                .size(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                PrimaryButton(text = "Next")
            }
        }
    }
}