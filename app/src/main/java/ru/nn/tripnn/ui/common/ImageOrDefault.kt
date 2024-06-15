package ru.nn.tripnn.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun ImageOrDefault(modifier: Modifier = Modifier, imageUrl: String?, grayPhoto: Boolean = false) {
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
            },
            colorFilter = if (grayPhoto) {
                ColorFilter.colorMatrix(
                    ColorMatrix().also { it.setToSaturation(0f) }
                )
            } else null
        )
    } else {
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.image_placeholder_primary),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            colorFilter = if (grayPhoto) {
                ColorFilter.colorMatrix(
                    ColorMatrix().also { it.setToSaturation(0f) }
                )
            } else null
        )
    }
}

@Preview
@Composable
private fun GrayPhotoPreview() {
    TripNNTheme {
        ImageOrDefault(imageUrl = "https://static.toiimg.com/photo/msid-103053443,width-96,height-65.cms", grayPhoto = true)
    }
}