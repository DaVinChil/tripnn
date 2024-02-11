package ru.nn.tripnn.ui.screen.application.general

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.local.preferences.UiPreferences
import ru.nn.tripnn.domain.repository.UiPreferencesRepository
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class GeneralUiViewModel @Inject constructor(
    private val uiPreferencesRepository: UiPreferencesRepository
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var message: String? = null
        private set
    var uiPreferencesState: UiPreferences by mutableStateOf(
        UiPreferences(
            theme = 2,
            id = 0L,
            currency = 0,
            language = 0
        )
    )
        private set

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            isLoading = true
            when (val result = uiPreferencesRepository.getUiPreferences()) {
                is Resource.Success -> {
                    uiPreferencesState = result.data ?: uiPreferencesState
                }

                is Resource.Error -> {
                    message = result.message
                }
            }
            isLoading = false
        }
    }

    fun changeTheme(theme: Int) {
        uiPreferencesState = uiPreferencesState.copy(theme = theme)
        viewModelScope.launch {
            uiPreferencesRepository.saveUiPreferences(uiPreferencesState)
        }
    }

    fun changeLanguage(language: Int) {
        uiPreferencesState = uiPreferencesState.copy(language = language)
        viewModelScope.launch {
            uiPreferencesRepository.saveUiPreferences(uiPreferencesState)
        }
    }

    fun changeCurrency(currency: Int) {
        uiPreferencesState = uiPreferencesState.copy(currency = currency)
        viewModelScope.launch {
            uiPreferencesRepository.saveUiPreferences(uiPreferencesState)
        }
    }

}