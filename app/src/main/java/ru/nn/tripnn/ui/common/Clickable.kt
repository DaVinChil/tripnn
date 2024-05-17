package ru.nn.tripnn.ui.common

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.nn.tripnn.ui.theme.LocalIndication

@Composable
fun Modifier.rippleClickable(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = LocalIndication.current,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = this.then(
    Modifier.clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClick = onClick
    )
)