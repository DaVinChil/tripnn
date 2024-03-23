package ru.nn.tripnn.ui.screen.main.settings

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.local.usersettings.UserSettings
import ru.nn.tripnn.data.local.usersettings.UserSettingsRepository
import ru.nn.tripnn.data.RemoteResource
import javax.inject.Inject

enum class Theme(@StringRes val resId: Int, val id: Int) {
    LIGHT(R.string.light_theme, 0), DARK(R.string.dark_theme, 1), SYSTEM(R.string.system_theme, 2)
}

enum class Language(@StringRes val resId: Int, val id: Int) {
    RUSSIAN(R.string.ru_lang, 0), ENGLISH(R.string.en_lang, 1)
}

enum class Currency(@StringRes val resId: Int, val id: Int) {
    RUB(R.string.rub, 0), USD(R.string.usd, 1)
}

fun <T> getEntryById(entries: List<T>, getId: T.() -> Int, id: Int): T {
    for(entry in entries) {
        if(entry.getId() == id) {
            return entry
        }
    }
    return entries.first()
}

fun getIsoLang(id: Int) = when (id) {
    Language.RUSSIAN.id -> "ru"
    Language.ENGLISH.id -> "en"
    else -> "en"
}

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
            theme = Theme.SYSTEM.id,
            currency = Currency.RUB.id,
            language = Language.ENGLISH.id
        )
    )
        private set

    fun loadPreferences() {
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

    fun changeTheme(theme: Int) {
        userSettingsState = userSettingsState.copy(theme = theme)
        viewModelScope.launch {
            userSettingsRepository.saveUserSettings(userSettingsState)
        }
    }

    fun changeLanguage(language: Int) {
        userSettingsState = userSettingsState.copy(language = language)
        viewModelScope.launch {
            userSettingsRepository.saveUserSettings(userSettingsState)
        }
    }

    fun changeCurrency(currency: Int) {
        userSettingsState = userSettingsState.copy(currency = currency)
        viewModelScope.launch {
            userSettingsRepository.saveUserSettings(userSettingsState)
        }
    }
}