package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.data.local.preferences.UiPreferences
import ru.nn.tripnn.domain.util.RemoteResource

interface UiPreferencesRepository {
    suspend fun getUiPreferences(): RemoteResource<UiPreferences>
    suspend fun saveUiPreferences(uiPreferences: UiPreferences)
}