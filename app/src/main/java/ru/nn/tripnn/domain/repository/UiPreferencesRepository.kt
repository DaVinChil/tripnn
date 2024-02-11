package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.data.local.preferences.UiPreferences
import ru.nn.tripnn.domain.util.Resource

interface UiPreferencesRepository {
    suspend fun getUiPreferences(): Resource<UiPreferences>
    suspend fun saveUiPreferences(uiPreferences: UiPreferences)
}