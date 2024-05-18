package ru.nn.tripnn.ui.screen.main.account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.UserInfoData
import ru.nn.tripnn.domain.state.ResState
import ru.nn.tripnn.ui.common.InternetProblemScreen
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.TwoButtonBottomSheetDialog
import ru.nn.tripnn.ui.common.rippleClickable
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme
import ru.nn.tripnn.ui.theme.montserratFamily

enum class DialogType {
    CHANGE_EMAIL, CHANGE_PASSWORD, CLEAR_HISTORY, DELETE_ACCOUNT, EXIT_DIALOG
}

val AVATAR_SIZE = 170.dp

@Composable
fun AccountScreen(
    userInfoData: ResState<UserInfoData>,
    onBackClick: () -> Unit,
    onUserNameChange: (String) -> Unit,
    onClearHistory: () -> Unit,
    onDeleteAccount: () -> Unit,
    onLeaveAccount: () -> Unit,
    onAvatarChange: (Uri) -> Unit
) {
    if (userInfoData.isError()) {
        InternetProblemScreen()
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TripNnTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        var dialogType by remember { mutableStateOf(DialogType.CHANGE_EMAIL) }
        var showDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.offset(x = (-16).dp, y = (-16).dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(R.string.back_txt),
                    tint = TripNnTheme.colorScheme.tertiary
                )
            }

            MontsText(
                text = stringResource(R.string.account_txt),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (userInfoData !is ResState.Success) {
                LoadingUserInfoBlock()
            } else {
                UserInfoBlock(
                    userInfoData = userInfoData.value,
                    onAvatarChange = onAvatarChange,
                    onUserNameChange = onUserNameChange
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
                MontsText(
                    text = stringResource(id = R.string.change_email),
                    fontSize = 16.sp,
                    modifier = Modifier.rippleClickable {
                        showDialog = true
                        dialogType = DialogType.CHANGE_EMAIL
                    }
                )
                MontsText(
                    text = stringResource(id = R.string.change_password),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.rippleClickable {
                        showDialog = true
                        dialogType = DialogType.CHANGE_PASSWORD
                    }
                )
                MontsText(
                    text = stringResource(id = R.string.clear_history),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.rippleClickable {
                        showDialog = true
                        dialogType = DialogType.CLEAR_HISTORY
                    }
                )
                MontsText(
                    text = stringResource(id = R.string.delete_account),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.rippleClickable {
                        showDialog = true
                        dialogType = DialogType.DELETE_ACCOUNT
                    }
                )
                MontsText(
                    text = stringResource(id = R.string.exit),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Red,
                    modifier = Modifier.rippleClickable {
                        showDialog = true
                        dialogType = DialogType.EXIT_DIALOG
                    }
                )
            }
        }

        if (showDialog) {
            when (dialogType) {
                DialogType.CHANGE_PASSWORD ->
                    ChangePasswordDialog(onSubmit = {}, onClose = { showDialog = false })

                DialogType.DELETE_ACCOUNT ->
                    DeleteAccountDialog(
                        onSubmit = onDeleteAccount,
                        onClose = { showDialog = false }
                    )

                DialogType.CLEAR_HISTORY ->
                    ClearHistoryDialog(onSubmit = onClearHistory, onClose = { showDialog = false })

                DialogType.EXIT_DIALOG ->
                    LeaveAccountDialog(onSubmit = onLeaveAccount, onClose = { showDialog = false })

                DialogType.CHANGE_EMAIL ->
                    ChangeEmailDialog(onSubmit = {}, onClose = { showDialog = false })
            }

        }
    }
}

@Composable
fun LoadingUserInfoBlock() {
    val shimmerInstance =
        rememberShimmer(shimmerBounds = ShimmerBounds.Window, theme = TripNnTheme.shimmer)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .size(AVATAR_SIZE)
                .background(TripNnTheme.colorScheme.minor)
                .shimmer(shimmerInstance)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .width(60.dp)
                .height(20.dp)
                .background(TripNnTheme.colorScheme.minor)
                .shimmer(shimmerInstance)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .width(150.dp)
                .height(20.dp)
                .background(TripNnTheme.colorScheme.minor)
                .shimmer(shimmerInstance)
        )
    }
}

@Composable
fun UserInfoBlock(
    userInfoData: UserInfoData,
    onAvatarChange: (Uri) -> Unit,
    onUserNameChange: (String) -> Unit
) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            it?.let { onAvatarChange(it) }
        }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(100))
                .size(AVATAR_SIZE)
                .rippleClickable {
                    launcher.launch("image/*")
                }
        ) {
            if (userInfoData.avatar == null) {
                Image(
                    painter = painterResource(id = R.drawable.account_avatar_placeholder),
                    contentDescription = stringResource(R.string.avatar)
                )
            } else {
                Image(
                    bitmap = userInfoData.avatar.asImageBitmap(),
                    contentDescription = stringResource(R.string.avatar)
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var name by remember { mutableStateOf(userInfoData.name) }

            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current

            BasicTextField(
                value = name,
                modifier = Modifier.focusRequester(focusRequester),
                onValueChange = {
                    if (it.length <= 20) {
                        name = it
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        this.defaultKeyboardAction(ImeAction.Done)
                        focusManager.clearFocus()
                        onUserNameChange(name)
                    }
                ),
                singleLine = true,
                textStyle = TextStyle(
                    color = TripNnTheme.colorScheme.textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = montserratFamily,
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(TripNnTheme.colorScheme.textColor)
            )

            Icon(
                painter = painterResource(id = R.drawable.edit_icon),
                contentDescription = stringResource(id = R.string.edit_name_txt),
                modifier = Modifier.rippleClickable { focusRequester.requestFocus() },
                tint = TripNnTheme.colorScheme.tertiary
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        MontsText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = userInfoData.email,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun ChangeEmailDialog(onSubmit: () -> Unit, onClose: () -> Unit) {
    TwoButtonBottomSheetDialog(
        title = "",
        text = "",
        rightButtonText = "",
        onSubmit = onSubmit,
        onClose = onClose
    )
}

@Composable
fun ChangePasswordDialog(onSubmit: () -> Unit, onClose: () -> Unit) {
    TwoButtonBottomSheetDialog(
        title = "",
        text = "",
        rightButtonText = "",
        onSubmit = onSubmit,
        onClose = onClose
    )
}

@Composable
fun ClearHistoryDialog(onSubmit: () -> Unit, onClose: () -> Unit) {
    TwoButtonBottomSheetDialog(
        title = stringResource(id = R.string.clear_history),
        text = stringResource(id = R.string.sure_to_delete_history_txt),
        rightButtonText = stringResource(id = R.string.clear_txt),
        onSubmit = onSubmit,
        onClose = onClose
    )
}

@Composable
fun DeleteAccountDialog(onSubmit: () -> Unit, onClose: () -> Unit) {
    TwoButtonBottomSheetDialog(
        title = stringResource(id = R.string.delete_account),
        text = stringResource(id = R.string.sure_to_delete_account_txt),
        rightButtonText = stringResource(id = R.string.delete),
        onSubmit = onSubmit,
        onClose = onClose
    )
}

@Composable
fun LeaveAccountDialog(onSubmit: () -> Unit, onClose: () -> Unit) {
    TwoButtonBottomSheetDialog(
        title = stringResource(id = R.string.exit),
        text = stringResource(id = R.string.sure_to_exit),
        rightButtonText = stringResource(id = R.string.exit),
        onSubmit = onSubmit,
        onClose = onClose
    )
}

@Preview
@Composable
fun AccountScreenPreview() {
    TripNNTheme {
        Surface {
            AccountScreen(
                onBackClick = {},
                userInfoData = ResState.Success(
                    UserInfoData(
                        name = "Sasha",
                        email = "hz.com@gmail.com",
                        avatar = null
                    )
                ),
                onUserNameChange = {},
                onClearHistory = {},
                onDeleteAccount = {},
                onLeaveAccount = {},
                onAvatarChange = {}
            )
        }
    }
}

@Preview
@Composable
fun LoadingUserInfo() {
    TripNNTheme {
        Box(modifier = Modifier.background(TripNnTheme.colorScheme.background)) {
            LoadingUserInfoBlock()
        }
    }
}