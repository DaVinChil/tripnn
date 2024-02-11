package ru.nn.tripnn.ui.screen.application

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.common.BaseBottomSheet
import ru.nn.tripnn.ui.common.BottomSheetState
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.theme.TripNNTheme

val THEMES = listOf("Светлая", "Темная", "Системная")
val LANGUAGES = listOf("Русский", "Английский")
val CURRENCY = listOf("Российский рубль", "Доллары")

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onThemeChange: (Int) -> Unit,
    onLanguageChange: (Int) -> Unit,
    onCurrencyChange: (Int) -> Unit,
    currentTheme: Int,
    currentLanguage: Int,
    currentCurrency: Int
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val bottomSheetState = rememberSettingsBottomSheetState(
            options = THEMES,
            title = "Тема",
            onChose = onThemeChange
        )

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

            MontsText(text = "Настройки", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(40.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Option(
                    title = "Тема",
                    current = THEMES[currentTheme],
                    onClick = {
                        bottomSheetState.options = THEMES
                        bottomSheetState.title = "Тема"
                        bottomSheetState.chosen = currentTheme
                        bottomSheetState.isShown = true
                        bottomSheetState.onChose = {
                            onThemeChange(it)
                        }
                    }
                )
                Option(
                    title = "Язык",
                    current = LANGUAGES[currentLanguage],
                    onClick = {
                        bottomSheetState.options = LANGUAGES
                        bottomSheetState.title = "Язык"
                        bottomSheetState.chosen = currentLanguage
                        bottomSheetState.isShown = true
                        bottomSheetState.onChose = {
                            onLanguageChange(it)
                        }
                    }
                )
                Option(
                    title = "Валюта",
                    current = CURRENCY[currentCurrency],
                    onClick = {
                        bottomSheetState.options = CURRENCY
                        bottomSheetState.chosen = currentCurrency
                        bottomSheetState.title = "Валюта"
                        bottomSheetState.isShown = true
                        bottomSheetState.onChose = {
                            onCurrencyChange(it)
                        }
                    }
                )
            }
        }

        SettingsBottomSheet(state = bottomSheetState)
    }
}

@Composable
fun Option(modifier: Modifier = Modifier, title: String, current: String, onClick: () -> Unit) {
    Column(modifier = modifier.fillMaxWidth().height(70.dp).clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center) {
        MontsText(text = title, fontSize = 18.sp)
        MontsText(text = current, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSecondary)
    }
}

@Composable
fun SettingsBottomSheet(state: SettingsBottomSheetState) {
    BaseBottomSheet(
        state = state,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    IconButton(
                        onClick = { state.isShown = false },
                        modifier = Modifier.size(40.dp)
                    ) {
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
            Spacer(Modifier.height(20.dp))
        }
    ) {
        Column(
            modifier = Modifier
                .shadow(borderRadius = 6.dp, blurRadius = 10.dp, color = Color.Black.copy(0.3f))
                .clip(RoundedCornerShape(6.dp))
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
        ) {
            for (i in 0 until state.options.size) {
                var background = Color.White
                var textColor = MaterialTheme.colorScheme.tertiary
                if (i == state.chosen) {
                    background = MaterialTheme.colorScheme.primary
                    textColor = Color.White
                }

                if (i != 0) {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 0.7.dp,
                        color = Color.Black.copy(0.01f)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .background(background)
                        .clickable(onClick = {
                            state.onChose(i)
                            state.chosen = i
                            state.isShown = false
                        })
                        .padding(start = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    MontsText(text = state.options[i], fontSize = 18.sp, color = textColor)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun rememberSettingsBottomSheetState(
    isShown: Boolean = false,
    options: List<String>,
    title: String,
    onChose: (Int) -> Unit,
    chosen: Int = -1
): SettingsBottomSheetState = remember {
    SettingsBottomSheetState(
        isShown = isShown, options = options,
        title = title, onChose = onChose, chosen = chosen
    )
}

class SettingsBottomSheetState(
    isShown: Boolean = false,
    var options: List<String>,
    var title: String,
    var onChose: (Int) -> Unit,
    chosen: Int = -1
) : BottomSheetState(isShown) {
    var chosen by mutableIntStateOf(chosen)
}

@Preview
@Composable
fun SettingsBottomSheetStatePreview() {
    TripNNTheme {
        val state = rememberSettingsBottomSheetState(
            isShown = true,
            options = listOf("Светлая", "Темная", "Системная"),
            title = "Тема",
            onChose = {}
        )
        SettingsBottomSheet(state = state)
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    TripNNTheme {
        Surface {
            SettingsScreen(
                onBackClick = {},
                onThemeChange = {},
                onLanguageChange = {},
                onCurrencyChange = {},
                currentTheme = 0,
                currentLanguage = 0,
                currentCurrency = 0
            )
        }
    }
}