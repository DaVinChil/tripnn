package ru.nn.tripnn.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.theme.TripNNTheme

@Composable
fun BaseBottomSheet(
    modifier: Modifier = Modifier,
    state: BottomSheetState,
    title: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val animationTime = 500

    val scrimAlpha by animateFloatAsState(
        targetValue = if (state.isShown) 0.3f else 0f,
        animationSpec = tween(animationTime),
        label = ""
    )

    Box(modifier = modifier.fillMaxSize()) {
        if (scrimAlpha > 0) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = scrimAlpha))
                .pointerInput(false) {
                    awaitPointerEventScope {
                        awaitPointerEvent()
                        state.isShown = false
                    }
                }
            )
        }

        AnimatedVisibility(
            visible = state.isShown,
            enter = slideInVertically(
                animationSpec = tween(animationTime),
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                animationSpec = tween(animationTime),
                targetOffsetY = { it }
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Color.White)
                    .navigationBarsPadding()
                    .pointerInput(false) {}
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
                    .onGloballyPositioned {
                        it.size.width
                    }
            ) {
                title()
                content()
            }
        }
    }
}

open class BottomSheetState(
    isShown: Boolean
) {
    var isShown by mutableStateOf(isShown)
}