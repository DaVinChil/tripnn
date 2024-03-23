package ru.nn.tripnn.ui.screen.main.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nn.tripnn.R
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.screen.main.account.BottomSheetDialog
import ru.nn.tripnn.ui.theme.TripNNTheme

enum class DialogType { THEME, LANGUAGE, CURRENCY }

data class Option(
    val text: String,
    val onClick: () -> Unit,
    val id: Int
)

@Composable
fun <T> toOptions(
    entries: List<T>,
    id: T.() -> Int,
    resId: T.() -> Int,
    onClick: (Int) -> Unit
): List<Option> {
    val options = mutableListOf<Option>()
    for (entry in entries) {
        options.add(
            Option(
                text = stringResource(id = entry.resId()),
                onClick = { onClick(entry.id()) },
                id = entry.id()
            )
        )
    }
    return options;
}

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
    var dialogState by remember { mutableStateOf(DialogType.THEME) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {

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
                currentCurrency = currentCurrency,
                currentLanguage = currentLanguage,
                currentTheme = currentTheme,
                openDialog = {
                    dialogState = it
                    showDialog = true
                },
            )
        }

        if (showDialog) {
            when (dialogState) {
                DialogType.THEME -> {
                    SettingsBottomSheetDialog(
                        onClose = { showDialog = false },
                        title = stringResource(id = R.string.theme),
                        options = toOptions(
                            entries = Theme.entries,
                            id = Theme::id,
                            resId = Theme::resId,
                            onClick = onThemeChange
                        ),
                        chosen = currentTheme
                    )
                }

                DialogType.CURRENCY -> {
                    SettingsBottomSheetDialog(
                        onClose = { showDialog = false },
                        title = stringResource(id = R.string.currency),
                        options = toOptions(
                            entries = Currency.entries,
                            id = Currency::id,
                            resId = Currency::resId,
                            onClick = onCurrencyChange
                        ),
                        chosen = currentCurrency
                    )
                }

                DialogType.LANGUAGE -> {
                    SettingsBottomSheetDialog(
                        onClose = { showDialog = false },
                        title = stringResource(id = R.string.language),
                        options = toOptions(
                            entries = Language.entries,
                            id = Language::id,
                            resId = Language::resId,
                            onClick = onLanguageChange
                        ),
                        chosen = currentLanguage
                    )
                }
            }
        }
    }
}

@Composable
fun Options(
    currentCurrency: Int,
    currentLanguage: Int,
    currentTheme: Int,
    openDialog: (DialogType) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        val themeTitle = stringResource(id = R.string.theme)
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
                openDialog(DialogType.THEME)
            }
        )

        val languageTitle = stringResource(id = R.string.language)
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
                openDialog(DialogType.LANGUAGE)
            }
        )

        val currencyTitle = stringResource(id = R.string.currency)
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
                openDialog(DialogType.CURRENCY)
            }
        )
    }
}

@Composable
fun Option(
    modifier: Modifier = Modifier,
    title: String,
    current: String,
    onClick: () -> Unit
) {
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
fun SettingsBottomSheetDialog(
    onClose: () -> Unit,
    title: String,
    options: List<Option>,
    chosen: Int
) {
    BottomSheetDialog(onClose = onClose) {
        MontsText(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .shadow(borderRadius = 6.dp, blurRadius = 10.dp, color = Color.Black.copy(0.3f))
                .clip(RoundedCornerShape(6.dp))
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
        ) {
            for (i in options.indices) {
                var background = Color.White
                var textColor = MaterialTheme.colorScheme.tertiary
                if (options[i].id == chosen) {
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
                        .clickable(onClick = { options[i].onClick() })
                        .padding(start = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    MontsText(text = options[i].text, fontSize = 18.sp, color = textColor)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview
@Composable
fun SettingsBottomSheetStatePreview() {
    TripNNTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

        }
        SettingsBottomSheetDialog(
            onClose = { },
            title = stringResource(id = R.string.language),
            options = toOptions(
                entries = Language.entries,
                id = Language::id,
                resId = Language::resId,
                onClick = { }
            ),
            chosen = 0
        )
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