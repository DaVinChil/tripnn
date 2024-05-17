package ru.nn.tripnn.ui.screen.main.photos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.nn.tripnn.R
import ru.nn.tripnn.data.datasource.stubdata.ui.PLACE_1
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.rippleClickable
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.theme.TripNNTheme
import kotlin.math.abs

@Composable
fun PhotosScreen(
    photos: ResourceState<List<String>>,
    initialPhoto: Int,
    onClose: () -> Unit
) {
    if (photos.state == null) {
        return
    }

    var isTopAppBarVisible by remember { mutableStateOf(true) }
    var curPhoto by remember(initialPhoto) { mutableIntStateOf(initialPhoto) }
    var swipeEnabled by remember { mutableStateOf(true) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { _ ->
                        isTopAppBarVisible = !isTopAppBarVisible
                    },
                    onDoubleTap = {
                        swipeEnabled = !swipeEnabled
                    }
                )
            }
            .swipeable(
                enabled = swipeEnabled,
                onLeftSwipe = {
                    curPhoto = minOf(photos.state.size - 1, curPhoto + 1)
                },
                onRightSwipe = {
                    curPhoto = maxOf(curPhoto - 1, 0)
                }
            )
    ) {
        PhotosTopAppBar(
            isVisible = isTopAppBarVisible,
            onClose = onClose,
            count = curPhoto + 1,
            outOf = photos.state.size
        )
        AsyncImage(
            model = photos.state[curPhoto],
            contentDescription = stringResource(id = R.string.image),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            contentScale = ContentScale.FillWidth
        )
    }

}

@Composable
fun PhotosTopAppBar(
    isVisible: Boolean,
    onClose: () -> Unit,
    count: Int,
    outOf: Int
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.3f))
                .statusBarsPadding()
                .padding(10.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                MontsText(
                    text = stringResource(id = R.string.close),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.rippleClickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onClose
                    ),
                    color = Color.White
                )
            }

            MontsText(
                text = count.toString() + " " + stringResource(id = R.string.of) + " " + outOf.toString(),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

fun Modifier.swipeable(enabled: Boolean = true, onLeftSwipe: () -> Unit, onRightSwipe: () -> Unit) =
    if (enabled) {
        this then Modifier.pointerInput(false) {
            var start: Long = 0
            var count = 0f

            detectHorizontalDragGestures(
                onDragStart = {
                    start = System.currentTimeMillis()
                },
                onDragEnd = {
                    val time =
                        System.currentTimeMillis() - start

                    if (abs(count) / time > 0.3) {
                        if (count < 0) {
                            onLeftSwipe()
                        } else {
                            onRightSwipe()
                        }
                    }

                    count = 0f
                },
                onDragCancel = {
                    count = 0f
                },
                onHorizontalDrag = { _, amount ->
                    count += amount
                }
            )
        }
    } else {
        this
    }


@Preview
@Composable
fun PhotosPreview() {
    TripNNTheme {
        PhotosScreen(photos = ResourceState(PLACE_1.photos), initialPhoto = 0) {

        }
    }
}