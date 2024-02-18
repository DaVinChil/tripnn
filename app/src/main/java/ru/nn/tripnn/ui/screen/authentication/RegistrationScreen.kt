package ru.nn.tripnn.ui.screen.authentication

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import ru.nn.tripnn.R
import ru.nn.tripnn.domain.entity.Credentials
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.PrimaryButton
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.montserratFamily

val SPACE_BETWEEN_INPUT = 22.dp

@Composable
fun RegistrationScreen(onSignUpClick: (Credentials) -> Unit, onSignInClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(start = 25.dp, end = 25.dp, top = 40.dp)
    ) {
        Title()

        Spacer(modifier = Modifier.height(64.dp))

        var email by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }
        var passRep by remember { mutableStateOf("") }

        Column(verticalArrangement = Arrangement.spacedBy(SPACE_BETWEEN_INPUT)) {
            InputBlock(
                value = email,
                title = stringResource(id = R.string.email),
                onValueChanged = { email = it },
                placeholder = stringResource(id = R.string.enter_email)
            )

            InputBlock(
                value = username,
                title = stringResource(id = R.string.enter_user_name),
                onValueChanged = { username = it },
                placeholder = stringResource(id = R.string.user_name)
            )

            PasswordInputBlock(
                value = pass,
                title = stringResource(id = R.string.password),
                onValueChanged = { pass = it },
                placeholder = stringResource(id = R.string.enter_password)
            )

            PasswordInputBlock(
                value = passRep,
                title = stringResource(id = R.string.confirm_password),
                onValueChanged = { passRep = it },
                placeholder = stringResource(id = R.string.confirm_password)
            )
        }

        Spacer(modifier = Modifier.height(52.dp))

        PrimaryButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.register),
            onClick = {
                if (passRep == pass) {
                    onSignUpClick(Credentials(name = username, email = email, password = pass))
                }
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        AlreadyHaveAccount(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onSignInClick = onSignInClick
        )
    }
}

@Composable
fun AlreadyHaveAccount(modifier: Modifier, onSignInClick: () -> Unit) {
    Row(modifier = modifier) {
        MontsText(
            text = stringResource(id = R.string.have_account),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.width(5.dp))
        MontsText(
            text = stringResource(id = R.string.login),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onSignInClick)
        )
    }
}

@Composable
private fun Title() {
    MontsText(
        text = stringResource(id = R.string.welcome),
        fontSize = 19.sp
    )
    Spacer(modifier = Modifier.height(15.dp))
    MontsText(
        text = stringResource(id = R.string.register),
        fontSize = 25.sp,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
fun InputBlock(
    value: String,
    title: String,
    onValueChanged: (String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column {
        MontsText(
            text = title,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        AuthInputField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            value = value,
            onValueChange = onValueChanged,
            placeholder = placeholder,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation
        )
    }
}

@Composable
fun PasswordInputBlock(
    value: String,
    title: String,
    onValueChanged: (String) -> Unit,
    placeholder: String
) {
    var passwordVisible by remember { mutableStateOf(false) }
    InputBlock(
        value = value,
        title = title,
        onValueChanged = onValueChanged,
        placeholder = placeholder,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                painterResource(id = R.drawable.outline_visibility)
            else
                painterResource(id = R.drawable.outline_visibility_off)

            val description =
                if (passwordVisible) stringResource(id = R.string.hide_password)
                else stringResource(id = R.string.show_password)

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = image, description)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthInputField(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: TextFieldColors = TextFieldDefaults.colors(
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        focusedContainerColor = MaterialTheme.colorScheme.secondary,
        unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSecondary,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSecondary
    )
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = true,
        textStyle = TextStyle(
            color = Color.Black,
            fontFamily = montserratFamily,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize
        )
    ) { innerTextField ->

        val icon: (@Composable () -> Unit)? = if (trailingIcon != null) {
            @Composable {
                Row {
                    trailingIcon()
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        } else {
            null
        }

        TextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = visualTransformation,
            innerTextField = innerTextField,
            singleLine = true,
            enabled = true,
            placeholder = {
                Text(
                    text = placeholder,
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = fontSize
                )
            },
            interactionSource = interactionSource,
            contentPadding = PaddingValues(start = 25.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            shape = RoundedCornerShape(100),
            colors = colors,
            trailingIcon = icon
        )
    }
}

@Preview
@Composable
fun InputFieldPreview() {
    TripNNTheme {
        AuthInputField(
            value = "",
            placeholder = "Password",
            onValueChange = { },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.outline_visibility_off),
                    contentDescription = "Visibility"
                )
            }
        )
    }
}

@Preview
@Composable
fun RegistrationScreenPreview() {
    TripNNTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            RegistrationScreen({}, {})
        }
    }
}