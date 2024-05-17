package ru.nn.tripnn.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.screen.main.account.LeaveAccountDialog
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoButtonBottomSheetDialog(
    title: String,
    text: String,
    leftButtonText: String = stringResource(id = R.string.cancel),
    rightButtonText: String,
    onSubmit: () -> Unit,
    onLeftButton: () -> Unit = {},
    onClose: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutine = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = TripNnTheme.colorScheme.bottomSheetBackground,
        windowInsets = WindowInsets(0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    IconButton(
                        onClick = {
                            coroutine.launch {
                                sheetState.hide()
                            }.invokeOnCompletion { onClose() }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(15.dp),
                            painter = painterResource(id = R.drawable.cross_gray),
                            contentDescription = stringResource(id = R.string.close_btm_sheet),
                            tint = Color.Unspecified
                        )
                    }
                }
                MontsText(text = title, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(44.dp))

            MontsText(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(44.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                PrimaryButton(
                    text = leftButtonText,
                    modifier = Modifier.height(55.dp).width(140.dp),
                    containerColor = TripNnTheme.colorScheme.cardBackground,
                    textColor = TripNnTheme.colorScheme.textColor,
                    onClick = {
                        onLeftButton()
                        coroutine.launch { sheetState.hide() }.invokeOnCompletion { onClose() }
                    }
                )
                PrimaryButton(
                    text = rightButtonText,
                    modifier = Modifier.height(55.dp),
                    onClick = {
                        onSubmit()
                        coroutine.launch { sheetState.hide() }.invokeOnCompletion { onClose() }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDialog(
    onClose: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutine = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = TripNnTheme.colorScheme.bottomSheetBackground,
        windowInsets = WindowInsets(0)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
        ) {
            IconButton(
                onClick = {
                    coroutine.launch { sheetState.hide() }.invokeOnCompletion { onClose() }
                }
            ) {
                Icon(
                    modifier = Modifier.size(15.dp),
                    painter = painterResource(id = R.drawable.cross_gray),
                    contentDescription = stringResource(id = R.string.close_btm_sheet),
                    tint = Color.Unspecified
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun BtmSheetPreview() {
    TripNNTheme {
        Surface {
            LeaveAccountDialog(onSubmit = {}, onClose = {})
        }
    }
}