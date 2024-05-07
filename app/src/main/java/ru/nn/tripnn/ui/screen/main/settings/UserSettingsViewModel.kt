package ru.nn.tripnn.ui.screen.main.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.data.local.usersettings.Currency
import ru.nn.tripnn.data.local.usersettings.Language
import ru.nn.tripnn.data.local.usersettings.Theme
import ru.nn.tripnn.data.local.usersettings.UserSettings
import ru.nn.tripnn.data.local.usersettings.UserSettingsRepository
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var message: String? = null
        private set

    var userSettingsState: UserSettings by mutableStateOf(
        UserSettings(
            theme = Theme.SYSTEM,
            currency = Currency.RUB,
            language = Language.RUSSIAN
        )
    )
        private set

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            isLoading = true
            when (val result = userSettingsRepository.getUserSettings()) {
                is RemoteResource.Success -> {
                    userSettingsState = result.data ?: userSettingsState
                }

                is RemoteResource.Error -> {
                    message = result.message
                }
            }
            isLoading = false
        }
    }

    fun changeTheme(theme: Theme) {
        changeUserSettings(theme = theme)
    }

    fun changeLanguage(language: Language) {
        changeUserSettings(language = language)
    }

    fun changeCurrency(currency: Currency) {
        changeUserSettings(currency = currency)
    }

    private fun changeUserSettings(
        theme: Theme = userSettingsState.theme,
        language: Language = userSettingsState.language,
        currency: Currency = userSettingsState.currency
    ) {
        userSettingsState = userSettingsState.copy(theme = theme, language = language, currency = currency)
        viewModelScope.launch {
            userSettingsRepository.saveUserSettings(userSettingsState)
        }
    }
}