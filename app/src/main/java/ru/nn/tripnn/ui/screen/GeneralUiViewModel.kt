package ru.nn.tripnn.ui.screen

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.local.preferences.UiPreferences
import ru.nn.tripnn.domain.repository.UiPreferencesRepository
import ru.nn.tripnn.domain.util.Resource
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
class GeneralUiViewModel @Inject constructor(
    private val uiPreferencesRepository: UiPreferencesRepository
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var message: String? = null
        private set
    var uiPreferencesState: UiPreferences by mutableStateOf(
        UiPreferences(
            theme = Theme.SYSTEM.id,
            currency = Currency.RUB.id,
            language = Language.RUSSIAN.id
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