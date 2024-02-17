package ru.nn.tripnn.ui.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.theme.TripNNTheme

@Composable
fun CatalogNavigation(
    modifier: Modifier = Modifier,
    catalogs: List<String>,
    onCatalogChange: (Int) -> Unit,
    chosen: Int
) {
    var catalogSize by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current.density
    val fontSize = 12.sp
    val selectorOffset by animateDpAsState(
        targetValue = (catalogSize / density * chosen).dp,
        label = ""
    )
    Box(
        Modifier
            .clip(RoundedCornerShape(100))
            .height(IntrinsicSize.Max)
            .then(modifier)
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Box(
            modifier = Modifier
                .offset(x = selectorOffset)
                .clip(RoundedCornerShape(100))
                .fillMaxHeight()
                .fillMaxWidth(1f / 3f)
                .background(MaterialTheme.colorScheme.primary)
        )
        Row(
            modifier = Modifier
        ) {
            catalogs.forEachIndexed { i, catalog ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { if (chosen != i) onCatalogChange(i) }
                        )
                        .padding(vertical = 8.dp)
                        .onGloballyPositioned { catalogSize = it.size.width }
                ) {
                    MontsText(
                        text = catalog,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = if (chosen == i) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.onSecondary,
                        fontSize = fontSize,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun CatalogNavigationPreview() {
    TripNNTheme(false) {
        var chosen by remember {
            mutableIntStateOf(0)
        }
        CatalogNavigation(
            modifier = Modifier.fillMaxWidth(),
            catalogs = listOf(
                stringResource(id = R.string.culture),
                stringResource(id = R.string.leisure),
                stringResource(id = R.string.to_eat)
            ),
            onCatalogChange = { chosen = it },
            chosen = chosen
        )
    }
}