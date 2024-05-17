package ru.nn.tripnn.data.repository.usersettings

import kotlinx.coroutines.flow.Flow
import ru.nn.tripnn.data.database.usersettings.Currency
import ru.nn.tripnn.data.database.usersettings.Language
import ru.nn.tripnn.data.database.usersettings.Theme
import ru.nn.tripnn.data.database.usersettings.UserSettings

interface UserSettingsRepository {
    fun getUserSettings(): Flow<Result<UserSettings?>>
    suspend fun setTheme(theme: Theme): Result<Unit>
    suspend fun setLanguage(language: Language): Result<Unit>
    suspend fun setCurrency(currency: Currency): Result<Unit>
}