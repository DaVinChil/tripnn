package ru.nn.tripnn.ui.screen.main.constructor

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.screen.main.search.SliderPointer
import ru.nn.tripnn.ui.theme.TripNNTheme
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val TODAY = 0
const val YESTERDAY = 1
const val DATE = 2

const val START_TIME = 0
const val END_TIME = 1

const val MAX_DIST = 1000f

@Composable
fun ConstructorScreen(
    onBack: () -> Unit,
    onContinue: (Restriction) -> Unit
) {
    val now = LocalDateTime.now()
    val dialogDateState = rememberMaterialDialogState()
    val dialogTimeState = rememberMaterialDialogState()
    var restriction by remember { mutableStateOf(Restriction()) }
    var chosenDate by remember { mutableIntStateOf(0) }
    var chosenTime by remember { mutableIntStateOf(START_TIME) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var infoClickOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    var infoPositionOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    var showScrim by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .padding(bottom = 30.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(42.dp)
        ) {
            Column {
                IconButton(onClick = onBack, modifier = Modifier.offset(x = (-16).dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = stringResource(id = R.string.back_txt),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }

                MontsText(
                    text = stringResource(id = R.string.customize_the_route),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Option(
                    isPicked = chosenDate == TODAY,
                    onClick = {
                        chosenDate = TODAY
                        restriction = restriction.copy(date = now.toLocalDate())
                    },
                    content = {
                        MontsText(
                            text = stringResource(id = R.string.today),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (chosenDate == TODAY) Color.White
                            else MaterialTheme.colorScheme.onSecondary
                        )
                    }
                )
                Option(
                    isPicked = chosenDate == YESTERDAY,
                    onClick = {
                        chosenDate = YESTERDAY
                        restriction = restriction.copy(date = now.plusDays(1).toLocalDate())
                    },
                    content = {
                        MontsText(
                            text = stringResource(id = R.string.yesterday),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (chosenDate == YESTERDAY) Color.White
                            else MaterialTheme.colorScheme.onSecondary
                        )
                    }
                )
                Option(
                    isPicked = chosenDate == DATE,
                    onClick = {
                        chosenDate = DATE
                        dialogDateState.show()
                    },
                    content = {
                        if (chosenDate == DATE) {
                            MontsText(
                                text = restriction.date.format(DateTimeFormatter.ofPattern("MM.dd")),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.calendar),
                                contentDescription = stringResource(id = R.string.calendar),
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                            )
                        }
                    },
                    paddings = if (chosenDate == DATE) PaddingValues(
                        horizontal = 30.dp,
                        vertical = 10.dp
                    )
                    else PaddingValues(10.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MontsText(
                    text = stringResource(id = R.string.from),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                TimeOption(value = restriction.startTime) {
                    chosenTime = START_TIME
                    dialogTimeState.show()
                }
                MontsText(
                    text = stringResource(id = R.string.until),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                TimeOption(value = restriction.endTime) {
                    chosenTime = END_TIME
                    dialogTimeState.show()
                }
            }

            var sliderWidth by remember { mutableFloatStateOf(0f) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        sliderWidth = it.size.width.toFloat()
                    }
            ) {
                MontsText(
                    text = stringResource(id = R.string.max_dist),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))

                SliderPointer(
                    sliderWidth = sliderWidth,
                    valueOnSlider = sliderPosition,
                    max = MAX_DIST,
                    infoPrefix = stringResource(id = R.string.metre_short)
                )
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 0f..MAX_DIST,
                    steps = 10
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                restriction =
                                    restriction.copy(considerCurrentLocation = !restriction.considerCurrentLocation)
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (restriction.considerCurrentLocation) R.drawable.selected
                            else R.drawable.not_selected
                        ),
                        contentDescription = stringResource(id = R.string.consider_location),
                        tint = if (restriction.considerCurrentLocation) Color.Unspecified
                        else MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(25.dp)

                    )

                    MontsText(
                        text = stringResource(id = R.string.consider_location),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                val infoInteractionSource = remember { MutableInteractionSource() }

                LaunchedEffect(infoInteractionSource) {
                    infoInteractionSource.interactions.collect {
                        if (it is PressInteraction.Press) {
                            infoClickOffset = it.pressPosition
                            showScrim = true
                        }
                    }
                }

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .onGloballyPositioned {
                            infoPositionOffset =
                                Offset(x = it.positionInRoot().x, y = it.positionInRoot().y)
                        },
                    interactionSource = infoInteractionSource
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = stringResource(id = R.string.info),
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        PrimaryButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = stringResource(id = R.string.next),
            onClick = { onContinue(restriction.copy(maxDistance = sliderPosition.toInt())) },
            paddingValues = PaddingValues(horizontal = 50.dp, vertical = 17.dp)
        )

        if (showScrim) {
            val density = LocalDensity.current.density
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { showScrim = false }
                    )
            )

            val dpClickOffset = (infoPositionOffset + infoClickOffset) / density
            println(infoClickOffset / density)

            ConsiderLocationInfo(
                offset = IntOffset(
                    x = dpClickOffset.x.toInt(),
                    y = dpClickOffset.y.toInt()
                ) - IntOffset(
                    x = LocalConfiguration.current.screenWidthDp,
                    y = 0
                ),
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }

    MaterialDialog(
        dialogState = dialogDateState,
        buttons = {
            positiveButton(text = stringResource(id = R.string.ok))
            negativeButton(text = stringResource(id = R.string.cancel))
        },
    ) {
        datepicker(
            initialDate = now.toLocalDate(),
            title = stringResource(id = R.string.pick_a_date),
            allowedDateValidator = {
                it.isAfter(now.minusDays(1).toLocalDate())
            }
        ) {
            restriction = restriction.copy(date = it)
        }
    }

    MaterialDialog(
        dialogState = dialogTimeState,
        buttons = {
            positiveButton(text = stringResource(id = R.string.ok))
            negativeButton(text = stringResource(id = R.string.cancel))
        }
    ) {
        timepicker(
            initialTime = now.toLocalTime(),
            title = stringResource(id = R.string.pick_the_time),
            timeRange = if (restriction.date == now.toLocalDate()) {
                now.toLocalTime()..LocalTime.MIDNIGHT.minusMinutes(1)
            } else {
                LocalTime.MIDNIGHT..LocalTime.MIDNIGHT.minusMinutes(1)
            }
        ) {
            restriction = if (chosenTime == START_TIME) {
                restriction.copy(startTime = it)
            } else {
                restriction.copy(endTime = it)
            }
        }
    }
}

@Composable
fun ConsiderLocationInfo(modifier: Modifier = Modifier, offset: IntOffset) {
    println(offset)
    Row(
        modifier = Modifier
            .offset { offset * this.density }
            .then(modifier)
            .shadow(color = Color.Black.copy(alpha = 0.1f), borderRadius = 8.dp, blurRadius = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth(5f / 6f)
            .background(MaterialTheme.colorScheme.background)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.info),
            contentDescription = stringResource(id = R.string.info),
            tint = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.width(10.dp))
        MontsText(text = stringResource(id = R.string.consider_location_full), fontSize = 14.sp)
    }
}

@Composable
fun Option(
    isPicked: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
    paddings: PaddingValues = PaddingValues(horizontal = 30.dp, vertical = 10.dp)
) {
    Box(
        modifier = Modifier
            .animateContentSize()
            .clip(CircleShape)
            .background(
                if (isPicked) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondary
                }
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(paddings)
    ) {
        content()
    }
}

@Composable
fun TimeOption(value: LocalTime, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(horizontal = 30.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        MontsText(
            text = value.format(DateTimeFormatter.ofPattern("hh:mm")),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Preview
@Composable
fun ConstructorPreview() {
    TripNNTheme(false) {
        ConstructorScreen(onBack = { /*TODO*/ }, onContinue = {})
    }
}