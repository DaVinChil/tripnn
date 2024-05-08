package ru.nn.tripnn.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme
import ru.nn.tripnn.ui.theme.montserratFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(modifier: Modifier = Modifier, onSearch: (String) -> Unit) {

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .then(modifier)
            .background(TripNnTheme.colorScheme.fieldBackground)
            .clickable { focusRequester.requestFocus() }
    ) {
        var searchValue by remember { mutableStateOf("") }
        BasicTextField(
            value = searchValue,
            modifier = Modifier.focusRequester(focusRequester),
            onValueChange = {
                if (it.length <= 20) {
                    searchValue = it
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    this.defaultKeyboardAction(ImeAction.Done)
                    focusManager.clearFocus()
                    onSearch(searchValue)
                }
            ),
            singleLine = true,
            textStyle = TextStyle(
                color = TripNnTheme.colorScheme.textColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = montserratFamily,
                textAlign = TextAlign.Start
            )
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = searchValue,
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                singleLine = true,
                enabled = true,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search),
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = TripNnTheme.colorScheme.hint,
                    )
                },
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(
                    start = 21.dp,
                    end = 20.dp,
                    top = 0.dp,
                    bottom = 0.dp
                ),
                shape = RoundedCornerShape(100),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = TripNnTheme.colorScheme.fieldBackground,
                    unfocusedContainerColor = TripNnTheme.colorScheme.fieldBackground,
                    focusedPlaceholderColor = TripNnTheme.colorScheme.hint,
                    unfocusedPlaceholderColor = TripNnTheme.colorScheme.hint
                ),
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(17.dp),
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = stringResource(id = R.string.search),
                        tint = Color.Unspecified
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun SearchPreview() {
    TripNNTheme {
        Search(onSearch = {}, modifier = Modifier.width(400.dp))
    }
}