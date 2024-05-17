package ru.nn.tripnn.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun ImageOrDefault(modifier: Modifier = Modifier, imageUrl: String?) {
    var tryImage by remember { mutableStateOf(true) }

    if (imageUrl != null && tryImage) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "image",
            modifier = modifier
                .background(TripNnTheme.colorScheme.undefined),
            contentScale = ContentScale.Crop,
            onError = {
                tryImage = false
            }
        )
    } else {
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.image_placeholder_primary),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )
    }
}