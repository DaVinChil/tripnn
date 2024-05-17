package ru.nn.tripnn.ui.screen.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.database.usersettings.Currency
import ru.nn.tripnn.data.database.usersettings.Language
import ru.nn.tripnn.data.database.usersettings.Theme
import ru.nn.tripnn.data.database.usersettings.UserSettings
import ru.nn.tripnn.data.repository.usersettings.UserSettingsRepository
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.util.toResourceStateFlow
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    val userSettings = userSettingsRepository.getUserSettings()
        .map { settings ->
            settings.also {
                if (it.getOrNull() == null)
                    Result.success(UserSettings())
            }
        }
        .toResourceStateFlow(viewModelScope, ResourceState(UserSettings()))

    fun changeTheme(theme: Theme) {
        viewModelScope.launch {
            userSettingsRepository.setTheme(theme = theme)
        }
    }

    fun changeLanguage(language: Language) {
        viewModelScope.launch {
            userSettingsRepository.setLanguage(language = language)
        }
    }

    fun changeCurrency(currency: Currency) {
        viewModelScope.launch {
            userSettingsRepository.setCurrency(currency)
        }
    }
}