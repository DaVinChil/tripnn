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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.entity.Credentials
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.montserratFamily

@Composable
fun LogInScreen(
    onForgotClick: () -> Unit,
    onLogInClick: (rememberMe: Boolean, credentials: Credentials) -> Unit,
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
                    title = stringResource(id = R.string.email),
                    onValueChanged = { email = it },
                    placeholder = stringResource(id = R.string.enter_email)
                )

                Spacer(modifier = Modifier.height(SPACE_BETWEEN_INPUT))

                PasswordInputBlock(
                    value = pass,
                    title = stringResource(id = R.string.password),
                    onValueChanged = { pass = it },
                    placeholder = stringResource(id = R.string.enter_password)
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
                            contentDescription = stringResource(id = R.string.remember),
                            tint = Color.Unspecified
                        )
                        MontsText(
                            text = stringResource(id = R.string.remember_me),
                            fontSize = 13.sp,
                        )
                    }

                    MontsText(
                        modifier = Modifier.clickable(onClick = onForgotClick),
                        text = stringResource(id = R.string.forgot_password),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            PrimaryButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.login),
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
        MontsText(
            text = stringResource(id = R.string.dont_have_account),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.width(5.dp))
        MontsText(
            text = stringResource(id = R.string.create),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onSignInClick)
        )
    }
}


@Composable
private fun Title() {
    Column {
        MontsText(
            text = stringResource(id = R.string.welcome_back),
            fontSize = 19.sp
        )
        Spacer(modifier = Modifier.height(15.dp))
        MontsText(
            text = stringResource(id = R.string.enter_account),
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold
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