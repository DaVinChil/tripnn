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
import ru.nn.tripnn.R
import ru.nn.tripnn.data.local.usersettings.Currency
import ru.nn.tripnn.data.local.usersettings.Language
import ru.nn.tripnn.data.local.usersettings.Theme
import ru.nn.tripnn.ui.common.MontsText
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.screen.main.account.BottomSheetDialog
import ru.nn.tripnn.ui.theme.TripNNTheme
import ru.nn.tripnn.ui.theme.TripNnTheme

enum class DialogType { THEME, LANGUAGE, CURRENCY }

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onThemeChange: (Theme) -> Unit,
    onLanguageChange: (Language) -> Unit,
    onCurrencyChange: (Currency) -> Unit,
    currentTheme: Theme,
    currentLanguage: Language,
    currentCurrency: Currency
) {
    var dialogState by remember { mutableStateOf(DialogType.THEME) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TripNnTheme.colorScheme.background)
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
                    contentDescription = stringResource(id = R.string.back_txt),
                    tint = TripNnTheme.colorScheme.tertiary
                )
            }

            MontsText(
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.titleMedium,
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
                        options = Theme.entries,
                        getId = Theme::id,
                        getResId = Theme::resId,
                        onChoose = onThemeChange,
                        chosen = currentTheme.id
                    )
                }

                DialogType.CURRENCY -> {
                    SettingsBottomSheetDialog(
                        onClose = { showDialog = false },
                        title = stringResource(id = R.string.currency),
                        options = Currency.entries,
                        getId = Currency::id,
                        getResId = Currency::resId,
                        onChoose = onCurrencyChange,
                        chosen = currentCurrency.id
                    )
                }

                DialogType.LANGUAGE -> {
                    SettingsBottomSheetDialog(
                        onClose = { showDialog = false },
                        title = stringResource(id = R.string.language),
                        options = Language.entries,
                        getId = Language::id,
                        getResId = Language::resId,
                        onChoose = onLanguageChange,
                        chosen = currentLanguage.id
                    )
                }
            }
        }
    }
}

@Composable
fun Options(
    currentCurrency: Currency,
    currentLanguage: Language,
    currentTheme: Theme,
    openDialog: (DialogType) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        val themeTitle = stringResource(id = R.string.theme)
        Option(
            title = themeTitle,
            current = stringResource(currentTheme.resId),
            onClick = {
                openDialog(DialogType.THEME)
            }
        )

        val languageTitle = stringResource(id = R.string.language)
        Option(
            title = languageTitle,
            current = stringResource(currentLanguage.resId),
            onClick = {
                openDialog(DialogType.LANGUAGE)
            }
        )

        val currencyTitle = stringResource(id = R.string.currency)
        Option(
            title = currencyTitle,
            current = stringResource(currentCurrency.resId),
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
        MontsText(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        MontsText(
            text = current,
            style = MaterialTheme.typography.labelSmall,
            color = TripNnTheme.colorScheme.onMinor
        )
    }
}

@Composable
fun <T> SettingsBottomSheetDialog(
    onClose: () -> Unit,
    title: String,
    options: List<T>,
    getId: T.() -> Int,
    getResId: T.() -> Int,
    onChoose: (T) -> Unit,
    chosen: Int
) {
    BottomSheetDialog(onClose = onClose) {
        MontsText(
            text = title,
            style = MaterialTheme.typography.displayLarge,
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
                var background = TripNnTheme.colorScheme.cardBackground
                var textColor = TripNnTheme.colorScheme.textColor
                if (options[i].getId() == chosen) {
                    background = TripNnTheme.colorScheme.primary
                    textColor = TripNnTheme.colorScheme.onPrimary
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
                        .clickable(onClick = { onChoose(options[i]) })
                        .padding(start = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    MontsText(
                        text = stringResource(id = options[i].getResId()),
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
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
                currentTheme = Theme.SYSTEM,
                currentLanguage = Language.RUSSIAN,
                currentCurrency = Currency.RUB
            )
        }
    }
}