package ru.nn.tripnn.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import ru.nn.tripnn.ui.theme.montserratFamily

@Composable
fun MontsText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = MaterialTheme.colorScheme.tertiary,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Visible,
    lineHeight: TextUnit? = null
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        fontFamily = montserratFamily,
        fontWeight = fontWeight,
        color = color,
        lineHeight = lineHeight ?: fontSize,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}