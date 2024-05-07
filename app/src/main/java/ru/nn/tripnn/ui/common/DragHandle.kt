package ru.nn.tripnn.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DragHandle() {
    Column {
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .width(67.dp)
                .height(3.dp)
                .background(Color(0xFFEBEBEB))
        )
        Spacer(Modifier.height(10.dp))
    }
}