package ru.nn.tripnn.ui.screen.main.account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.UserInfoData
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.screen.authentication.ResourceState
import ru.nn.tripnn.ui.screen.main.home.InternetProblem
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.montserratFamily
import ru.nn.tripnn.ui.util.darkShimmer

enum class DialogType {
    CHANGE_EMAIL, CHANGE_PASSWORD, CLEAR_HISTORY, DELETE_ACCOUNT, EXIT_DIALOG
}

val AVATAR_SIZE = 170.dp

@Composable
fun AccountScreen(
    userInfoData: ResourceState<UserInfoData>,
    onBackClick: () -> Unit,
    onUserNameChange: (String) -> Unit,
    onClearHistory: () -> Unit,
    onDeleteAccount: () -> Unit,
    onLeaveAccount: () -> Unit,
    onAvatarChange: (Uri) -> Unit
) {
    if (userInfoData.isError || (!userInfoData.isLoading && userInfoData.value == null)) {
        InternetProblem()
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                    contentDescription = stringResource(R.string.back_txt)
                )
            }

            MontsText(
                text = stringResource(R.string.account_txt),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (userInfoData.isLoading) {
                LoadingUserInfoBlock()
            } else {
                UserInfoBlock(
                    userInfoData = userInfoData.value ?: UserInfoData(name = "", email = "", avatar = null),
                    onAvatarChange = onAvatarChange,
                    onUserNameChange = onUserNameChange
                )
            }

            Spacer(modifier = Modifier.height(40.dp))


            Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
                MontsText(
                    text = stringResource(id = R.string.change_email),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        showDialog = true
                        dialogType = DialogType.CHANGE_EMAIL
                    }
                )
                MontsText(
                    text = stringResource(id = R.string.change_password),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        showDialog = true
                        dialogType = DialogType.CHANGE_PASSWORD
                    }
                )
                MontsText(
                    text = stringResource(id = R.string.clear_history),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        showDialog = true
                        dialogType = DialogType.CLEAR_HISTORY
                    }
                )
                MontsText(
                    text = stringResource(id = R.string.delete_account),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        showDialog = true
                        dialogType = DialogType.DELETE_ACCOUNT
                    }
                )
                MontsText(
                    text = stringResource(id = R.string.exit),
                    fontSize = 16.sp,
                    color = Color.Red,
                    modifier = Modifier.clickable {
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
                        onClose = { showDialog = false })

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
        rememberShimmer(shimmerBounds = ShimmerBounds.Window, theme = darkShimmer)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .size(AVATAR_SIZE)
                .background(MaterialTheme.colorScheme.secondary)
                .shimmer(shimmerInstance)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .width(60.dp)
                .height(20.dp)
                .background(MaterialTheme.colorScheme.secondary)
                .shimmer(shimmerInstance)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .width(150.dp)
                .height(20.dp)
                .background(MaterialTheme.colorScheme.secondary)
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
                .clickable {
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
            var name by remember {
                mutableStateOf(userInfoData.name)
            }
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
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = montserratFamily,
                    textAlign = TextAlign.Center
                )
            )

            Icon(
                painter = painterResource(id = R.drawable.edit_icon),
                contentDescription = stringResource(id = R.string.edit_name_txt),
                modifier = Modifier.clickable { focusRequester.requestFocus() }
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        MontsText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = userInfoData.email,
            fontSize = 14.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoButtonBottomSheetDialog(
    title: String,
    text: String,
    rightButtonText: String,
    onSubmit: () -> Unit,
    onClose: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutine = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = MaterialTheme.colorScheme.background,
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
                }
                MontsText(text = title, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(44.dp))

            MontsText(
                text = text,
                fontSize = 16.sp,
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
                    text = stringResource(id = R.string.cancel),
                    modifier = Modifier
                        .height(55.dp)
                        .width(140.dp),
                    containerColor = MaterialTheme.colorScheme.background,
                    textColor = MaterialTheme.colorScheme.tertiary,
                    onClick = {
                        coroutine.launch { sheetState.hide() }.invokeOnCompletion { onClose() }
                    }
                )
                PrimaryButton(
                    text = rightButtonText,
                    modifier = Modifier
                        .height(55.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
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
        containerColor = MaterialTheme.colorScheme.background,
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

@Preview
@Composable
fun AccountScreenPreview() {
    TripNNTheme {
        Surface {
            AccountScreen(
                onBackClick = {},
                userInfoData = ResourceState(
                    value = UserInfoData(
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
    TripNNTheme (false) {
        Box(modifier = Modifier.background(Color.White)) {
            LoadingUserInfoBlock()
        }
    }
}