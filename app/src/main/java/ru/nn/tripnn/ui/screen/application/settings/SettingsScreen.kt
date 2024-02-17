package ru.nn.tripnn.ui.screen.application.settings

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.common.BaseBottomSheet
import ru.nn.tripnn.ui.common.BottomSheetState
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.screen.Currency
import ru.nn.tripnn.ui.screen.Language
import ru.nn.tripnn.ui.screen.Theme
import ru.nn.tripnn.ui.screen.getEntryById
import ru.nn.tripnn.ui.theme.TripNNTheme

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        val bottomSheetState = rememberSettingsBottomSheetState(
            options = listOf(),
            title = ""
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
                    contentDescription = stringResource(id = R.string.back_txt)
                )
            }

            MontsText(
                text = stringResource(id = R.string.settings),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(40.dp))

            Options(
                bottomSheetState,
                currentCurrency,
                currentLanguage,
                currentTheme,
                onCurrencyChange,
                onLanguageChange,
                onThemeChange
            )
        }

        SettingsBottomSheet(state = bottomSheetState)
    }
}

@Composable
fun Options(
    bottomSheetState: SettingsBottomSheetState,
    currentCurrency: Int,
    currentLanguage: Int,
    currentTheme: Int,
    onCurrencyChange: (Int) -> Unit,
    onLanguageChange: (Int) -> Unit,
    onThemeChange: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        val themeTitle = stringResource(id = R.string.theme)
        val themeOptions = toOptions(
            entries = Theme.entries,
            id = Theme::id,
            resId = Theme::resId,
            onClick = onThemeChange
        )
        Option(
            title = themeTitle,
            current = stringResource(
                getEntryById(
                    Theme.entries,
                    Theme::id,
                    currentTheme
                ).resId
            ),
            onClick = {
                bottomSheetState.options = themeOptions
                bottomSheetState.title = themeTitle
                bottomSheetState.chosen = currentTheme
                bottomSheetState.isShown = true
            }
        )

        val languageTitle = stringResource(id = R.string.language)
        val languageOptions = toOptions(
            entries = Language.entries,
            id = Language::id,
            resId = Language::resId,
            onClick = onLanguageChange
        )
        Option(
            title = languageTitle,
            current = stringResource(
                getEntryById(
                    Language.entries,
                    Language::id,
                    currentLanguage
                ).resId
            ),
            onClick = {
                bottomSheetState.options = languageOptions
                bottomSheetState.title = languageTitle
                bottomSheetState.chosen = currentLanguage
                bottomSheetState.isShown = true
            }
        )

        val currencyTitle = stringResource(id = R.string.currency)
        val currencyOptions = toOptions(
            entries = Currency.entries,
            id = Currency::id,
            resId = Currency::resId,
            onClick = onCurrencyChange
        )
        Option(
            title = currencyTitle,
            current = stringResource(
                getEntryById(
                    Currency.entries,
                    Currency::id,
                    currentCurrency
                ).resId
            ),
            onClick = {
                bottomSheetState.options = currencyOptions
                bottomSheetState.chosen = currentCurrency
                bottomSheetState.title = currencyTitle
                bottomSheetState.isShown = true
            }
        )
    }
}

@Composable
fun Option(modifier: Modifier = Modifier, title: String, current: String, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center
    ) {
        MontsText(text = title, fontSize = 18.sp)
        MontsText(text = current, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSecondary)
    }
}

@Composable
fun <T> toOptions(
    entries: List<T>,
    id: T.() -> Int,
    resId: T.() -> Int,
    onClick: (Int) -> Unit
): List<Option> {
    val options = mutableListOf<Option>()
    for (entry in entries) {
        options.add(Option(
            text = stringResource(id = entry.resId()),
            onClick = { onClick(entry.id()) }
        ))
    }
    return options;
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
                            contentDescription = stringResource(id = R.string.close_btm_sheet),
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
                            state.options[i].onClick()
                            state.chosen = i
                            state.isShown = false
                        })
                        .padding(start = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    MontsText(text = state.options[i].text, fontSize = 18.sp, color = textColor)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun rememberSettingsBottomSheetState(
    isShown: Boolean = false,
    options: List<Option>,
    title: String,
    chosen: Int = -1
): SettingsBottomSheetState = remember {
    SettingsBottomSheetState(
        isShown = isShown, options = options,
        title = title, chosen = chosen
    )
}

data class Option(
    val text: String,
    val onClick: () -> Unit
)

class SettingsBottomSheetState(
    isShown: Boolean = false,
    var options: List<Option>,
    var title: String,
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
            options = toOptions(
                entries = Theme.entries,
                id = Theme::id,
                resId = Theme::resId,
                onClick = {}),
            title = stringResource(id = R.string.theme)
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