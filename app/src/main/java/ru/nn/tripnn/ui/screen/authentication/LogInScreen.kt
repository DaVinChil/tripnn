package ru.nn.tripnn.ui.screen.authentication

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.entity.Credentials
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.montserratFamily

@Composable
fun LogInScreen(
    onForgotClick: () -> Unit,
    onLogInClick: (rememeberMe: Boolean, credentials: Credentials) -> Unit,
    onRegisterClick: () -> Unit
) {
    SystemBarsToBackgroundColor()

    var rememberMe by remember {
        mutableStateOf(false)
    }
    val onRememberClick = { rememberMe = !rememberMe }

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 25.dp, end = 25.dp, top = 40.dp, bottom = 40.dp)
    ) {
        Column {
            Title()

            Spacer(modifier = Modifier.height(64.dp))

            Column {
                InputBlock(
                    value = email,
                    title = "Эл. почта",
                    onValueChanged = { email = it },
                    placeholder = "Введите почту"
                )

                Spacer(modifier = Modifier.height(SPACE_BETWEEN_INPUT))

                PasswordInputBlock(
                    value = pass,
                    title = "Пароль",
                    onValueChanged = { pass = it },
                    placeholder = "Введите пароль"
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.clickable(onClick = onRememberClick),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        val painter = if (rememberMe) painterResource(id = R.drawable.selected)
                        else painterResource(id = R.drawable.not_selected)
                        Icon(
                            painter = painter,
                            contentDescription = "Remember",
                            tint = Color.Unspecified
                        )
                        Text(
                            text = "Запомнить меня",
                            fontSize = 13.sp,
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }

                    Text(
                        modifier = Modifier.clickable(onClick = onForgotClick),
                        text = "Забыли пароль ?",
                        fontSize = 13.sp,
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            PrimaryButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Войти",
                onClick = { onLogInClick(rememberMe, Credentials(email = email, password = pass)) }
            )

            Spacer(modifier = Modifier.height(30.dp))

            DontHaveAccount(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onSignInClick = onRegisterClick
            )
        }
    }
}


@Composable
fun DontHaveAccount(modifier: Modifier, onSignInClick: () -> Unit) {
    Row(modifier = modifier) {
        Text(
            text = "Еще нет аккаунта ?",
            fontSize = 12.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "Создать",
            fontSize = 12.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.clickable(onClick = onSignInClick)
        )
    }
}


@Composable
private fun Title() {
    Column {
        Text(
            text = "С возвращением !",
            fontSize = 19.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Войдите в аккаунт",
            fontSize = 25.sp,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Preview
@Composable
fun LogInScreenPreview() {
    TripNNTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LogInScreen(
                onForgotClick = { },
                onLogInClick = {a, b -> },
                onRegisterClick = {}
            )
        }
    }
}