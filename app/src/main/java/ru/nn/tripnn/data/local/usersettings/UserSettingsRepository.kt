package ru.nn.tripnn.data.local.usersettings

import ru.nn.tripnn.data.RemoteResource

interface UserSettingsRepository {
    suspend fun getUserSettings(): RemoteResource<UserSettings>
    suspend fun saveUserSettings(userSettings: UserSettings)
}