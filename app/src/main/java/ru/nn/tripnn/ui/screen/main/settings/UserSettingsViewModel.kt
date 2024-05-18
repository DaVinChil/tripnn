package ru.nn.tripnn.ui.screen.main.settings

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.database.usersettings.Currency
import ru.nn.tripnn.data.database.usersettings.Language
import ru.nn.tripnn.data.database.usersettings.Theme
import ru.nn.tripnn.data.database.usersettings.UserSettings
import ru.nn.tripnn.data.repository.usersettings.UserSettingsRepository
import ru.nn.tripnn.domain.state.ResState
import ru.nn.tripnn.domain.state.toResStateFlow
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _userSettings: MutableStateFlow<StateFlow<ResState<UserSettings>>> = MutableStateFlow(requestUserSettings())

    val userSettings
        @Composable
        get() = _userSettings.collectAsStateWithLifecycle().value

    private fun requestUserSettings(): StateFlow<ResState<UserSettings>> {
        return userSettingsRepository.getUserSettings()
            .map { settings ->
                settings.also {
                    if (it.getOrNull() == null)
                        Result.success(UserSettings())
                }
            }
            .toResStateFlow(viewModelScope, ResState.Success(UserSettings()))
    }

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