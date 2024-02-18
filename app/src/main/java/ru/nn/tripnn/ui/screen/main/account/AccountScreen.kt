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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.entity.UserInfo
import ru.nn.tripnn.ui.common.BaseBottomSheet
import ru.nn.tripnn.ui.common.BottomSheetState
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.montserratFamily

@Composable
fun AccountScreen(
    userState: UserState,
    onBackClick: () -> Unit,
    onUserNameChange: (String) -> Unit,
    onClearHistory: () -> Unit,
    onDeleteAccount: () -> Unit,
    onLeaveAccount: () -> Unit,
    onAvatarChange: (Uri) -> Unit
) {
    if (!userState.isLoading && userState.userInfo != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
        ) {
            val userInfo = userState.userInfo

            val btmSheetState = rememberTwoButtonBottomSheetState(
                title = "",
                text = "",
                rightButtonText = "",
                onSubmit = {},
                isShown = false
            )

            val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
                    it?.let { onAvatarChange(it) }
                }

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
                        contentDescription = "back"
                    )
                }

                MontsText(text = "Аккаунт", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(40.dp))

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(100))
                        .clickable {
                            launcher.launch("image/*")
                        }
                ) {
                    if (userInfo.avatar == null) {
                        Image(
                            painter = painterResource(id = R.drawable.account_avatar_placeholder),
                            contentDescription = "avatar"
                        )
                    } else {
                        Image(
                            bitmap = userInfo.avatar.asImageBitmap(),
                            contentDescription = "avatar"
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
                        mutableStateOf(userInfo.name)
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
                        contentDescription = "edit name",
                        modifier = Modifier.clickable { focusRequester.requestFocus() }
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                MontsText(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = userInfo.email,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(40.dp))


                Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
                    MontsText(
                        text = "Изменить адрес эл. почты",
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {

                        }
                    )
                    MontsText(
                        text = "Изменить пароль",
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {

                        }
                    )
                    MontsText(
                        text = "Очистить историю",
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            btmSheetState.title = "Очистить историю"
                            btmSheetState.text =
                                "Вы уверены, что хотите очистить историю? Будут удалены все сведенья о посещенных местах и пройденных маршрутах"
                            btmSheetState.rightButtonText = "Очистить"
                            btmSheetState.onSubmit = onClearHistory
                            btmSheetState.isShown = true
                        }
                    )
                    MontsText(
                        text = "Удалить аккаунт",
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            btmSheetState.title = "Удалить аккаунт"
                            btmSheetState.text = "Вы уверены, что хотите удалить аккаунт?"
                            btmSheetState.rightButtonText = "Удалить"
                            btmSheetState.onSubmit = onDeleteAccount
                            btmSheetState.isShown = true
                        }
                    )
                    MontsText(
                        text = "Выйти",
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier.clickable {
                            btmSheetState.title = "Выйти"
                            btmSheetState.text = "Вы уверены, что хотите выйти из аккаунта?"
                            btmSheetState.rightButtonText = "Выйти"
                            btmSheetState.onSubmit = onLeaveAccount
                            btmSheetState.isShown = true
                        }
                    )
                }
            }

            TwoButtonBottomSheet(state = btmSheetState)
        }
    }
}

@Composable
fun TwoButtonBottomSheet(
    modifier: Modifier = Modifier,
    state: TwoButtonBottomSheetState
) {
    BaseBottomSheet(
        modifier = modifier,
        state = state,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    IconButton(onClick = { state.isShown = false }) {
                        Icon(
                            modifier = Modifier.size(15.dp),
                            painter = painterResource(id = R.drawable.cross_gray),
                            contentDescription = "close bottom sheet",
                            tint = Color.Unspecified
                        )
                    }
                }
                MontsText(text = state.title, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
            }
        },
    ) {
        Spacer(modifier = Modifier.height(44.dp))

        MontsText(
            text = state.text,
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
                text = "Отмена",
                modifier = Modifier
                    .height(55.dp)
                    .width(140.dp),
                containerColor = Color.White,
                textColor = MaterialTheme.colorScheme.tertiary,
                onClick = { state.isShown = false }
            )
            PrimaryButton(
                text = state.rightButtonText,
                modifier = Modifier
                    .height(55.dp)
                    .width(140.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    state.onSubmit()
                    state.isShown = false
                }
            )
        }
    }
}

@Composable
fun rememberTwoButtonBottomSheetState(
    isShown: Boolean = false,
    title: String = "",
    text: String = "",
    rightButtonText: String = "",
    onSubmit: () -> Unit = {}
): TwoButtonBottomSheetState = remember {
    TwoButtonBottomSheetState(
        isShown = isShown,
        title = title,
        text = text,
        rightButtonText = rightButtonText,
        onSubmit = onSubmit
    )
}

class TwoButtonBottomSheetState(
    isShown: Boolean,
    var title: String,
    var text: String,
    var rightButtonText: String,
    var onSubmit: () -> Unit
) : BottomSheetState(isShown)

@Preview
@Composable
fun BtmSheetPreview() {
    TripNNTheme {
        Surface {
            val state = rememberTwoButtonBottomSheetState(
                isShown = true,
                title = "Выйти",
                text = "Вы уверены, что хотите выйти из аккаунта?",
                rightButtonText = "Выйти",
                onSubmit = { }
            )
            TwoButtonBottomSheet(state = state)
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
                userState = UserState(
                    userInfo = UserInfo(
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