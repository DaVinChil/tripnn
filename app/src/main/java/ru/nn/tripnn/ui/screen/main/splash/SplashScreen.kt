package ru.nn.tripnn.ui.screen.main.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.R

@Composable
fun HeartSplashScreen(onFinish: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .requiredWidth(408.dp)
                .requiredWidth(116.dp),
            painter = painterResource(id = R.drawable.tripnn_logo),
            contentDescription = stringResource(id = R.string.logo_txt),
            tint = Color.Unspecified
        )

        val size = remember { Animatable(0f) }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100))
                .requiredSize(size.value.dp)
                .background(Color(0xFFFF8967)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .requiredWidth(408.dp)
                    .requiredWidth(116.dp),
                painter = painterResource(id = R.drawable.tripnn_logo),
                contentDescription = stringResource(id = R.string.logo_txt),
                tint = Color.White
            )
        }

        LaunchedEffect(Unit) {
            size.animateTo(300f, tween(1000))
            repeat(2) {
                size.animateTo(200f, tween(1000))
                size.animateTo(300f, tween(1000))
            }
            size.animateTo(1000f, tween(1000))
            onFinish()
        }
    }
}