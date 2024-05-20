package ru.nn.tripnn.ui.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.datasource.stubdata.ui.PLACE_1
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlaceInfoBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    place: Place,
    removeFromFavourite: () -> Unit,
    addToFavourite: () -> Unit,
    toPhotos: (String, Int) -> Unit
) {
    var showSnackBar by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { DragHandle() },
        containerColor = Color.Transparent,
        windowInsets = WindowInsets(0)
    ) {
        Box {
            Column(modifier = Modifier.fillMaxWidth()) {
                var tryImage by remember { mutableStateOf(true) }
                if (place.photos.isNotEmpty() && tryImage) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        itemsIndexed(items = place.photos, key = { _, url -> url }) { index, url ->
                            AsyncImage(
                                model = url,
                                contentDescription = stringResource(id = R.string.image),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(TripNnTheme.colorScheme.onMinor)
                                    .width(220.dp)
                                    .height(160.dp)
                                    .rippleClickable {
                                        toPhotos(place.id, index)
                                    },
                                contentScale = ContentScale.Crop,
                                onError = { tryImage = false }
                            )
                        }
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.image_placeholder_primary),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .width(220.dp)
                            .height(160.dp)
                    )
                }


                Spacer(modifier = Modifier.height(5.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                topStart = 15.dp, topEnd = 15.dp,
                                bottomEnd = 0.dp, bottomStart = 0.dp
                            )
                        )
                        .background(TripNnTheme.colorScheme.bottomSheetBackground)
                        .padding(top = 10.dp)
                        .navigationBarsPadding()
                ) {
                    var favourite by rememberSaveable { mutableStateOf(place.favourite) }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        MontsText(
                            text = place.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            modifier = Modifier
                                .fillMaxWidth(4 / 6f)
                                .basicMarquee()
                        )

                        Spacer(modifier = Modifier.width(15.dp))

                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .rippleClickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        favourite = if (favourite) {
                                            removeFromFavourite()
                                            false
                                        } else {
                                            addToFavourite()
                                            true
                                        }
                                    }
                                ),
                            painter = painterResource(
                                id = if (favourite) R.drawable.gold_bookmark
                                else R.drawable.gray_bookmark
                            ),
                            contentDescription = stringResource(id = R.string.is_favourtie),
                            tint = Color.Unspecified
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            if (place.type != null) {
                                MontsText(
                                    text = place.type,
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.heightIn(15.dp))

                            TwoGisButton(doubleGisLink = place.doubleGisLink)

                            Spacer(modifier = Modifier.height(5.dp))

                            Row {
                                Rating(
                                    rating = place.rating,
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                MontsText(
                                    text = place.reviews.toString() + " " +
                                            stringResource(id = R.string.reviews),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {

                            if (place.address != null) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    MontsText(
                                        text = place.address,
                                        style = MaterialTheme.typography.labelSmall,
                                        maxLines = 1,
                                        modifier = Modifier.basicMarquee()
                                    )
                                    CopyIcon(
                                        data = place.address,
                                        onClick = { showSnackBar = true }
                                    )
                                }
                            }

                            if (place.workTime != null) {
                                MontsText(
                                    text = place.workTime,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            if (place.phone != null) {
                                MontsText(
                                    text = place.phone,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            if (place.cost != null) {
                                MontsText(
                                    text = stringResource(id = R.string.avg_receipt) + " " + place.cost + "₽",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            if (showSnackBar) {
                CopiedToClipBoardSnackBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onHide = { showSnackBar = false })
            }
        }
    }
}

@Composable
fun CopyIcon(data: String, onClick: () -> Unit) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    Icon(
        painter = painterResource(id = R.drawable.copy_icon),
        contentDescription = stringResource(id = R.string.copy),
        tint = TripNnTheme.colorScheme.onMinor,
        modifier = Modifier.rippleClickable(
            indication = rememberRipple(radius = 1.dp, color = Color.White),
            interactionSource = remember { MutableInteractionSource() },
            onClick = {
                clipboardManager.setText(buildAnnotatedString { append(data) })
                onClick()
            }
        )
    )
}

@Composable
fun CopiedToClipBoardSnackBar(modifier: Modifier = Modifier, onHide: () -> Unit) {
    val startOffset = 80f
    val offset = remember { Animatable(startOffset) }
    val alpha = remember { Animatable(0f) }
    val localOnHide by rememberUpdatedState(newValue = onHide)
    val time = 2000L

    Row(
        modifier = Modifier
            .offset(y = offset.value.dp - 10.dp)
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .alpha(alpha.value)
            .then(modifier)
            .fillMaxWidth()
            .background(TripNnTheme.colorScheme.primary)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.info),
            contentDescription = stringResource(id = R.string.info),
            tint = TripNnTheme.colorScheme.onPrimary
        )
        MontsText(
            text = stringResource(id = R.string.address_copied),
            style = MaterialTheme.typography.labelMedium,
            color = TripNnTheme.colorScheme.onPrimary
        )
    }

    LaunchedEffect(Unit) {
        launch { alpha.animateTo(1f, tween(500)) }
        offset.animateTo(
            0f,
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        delay(time)

        launch { alpha.animateTo(0f, tween(300)) }
        offset.animateTo(
            startOffset,
            tween(500)
        )

        localOnHide()
    }
}

@Composable
fun TwoGisButton(modifier: Modifier = Modifier, doubleGisLink: String) {
    val uriHandler = LocalUriHandler.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.rippleClickable { uriHandler.openUri(doubleGisLink) }
    ) {
        MontsText(
            text = "2GIS",
            style = MaterialTheme.typography.labelMedium
        )
        Icon(
            painter = painterResource(id = R.drawable.reversed_link_icon),
            contentDescription = "",
            tint = TripNnTheme.colorScheme.tertiary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CardInfoBottomSheetPreview() {
    TripNNTheme {
        Box(modifier = Modifier.background(TripNnTheme.colorScheme.background)) {
            PlaceInfoBottomSheet(
                place = PLACE_1,
                removeFromFavourite = { },
                addToFavourite = {},
                sheetState = rememberModalBottomSheetState(),
                onDismissRequest = {},
                toPhotos = { _, _ -> }
            )
        }
    }
}