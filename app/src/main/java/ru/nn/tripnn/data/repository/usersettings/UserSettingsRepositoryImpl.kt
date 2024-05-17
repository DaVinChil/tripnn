package ru.nn.tripnn.data.repository.usersettings

import kotlinx.coroutines.flow.Flow
import ru.nn.tripnn.data.database.usersettings.Currency
import ru.nn.tripnn.data.database.usersettings.Language
import ru.nn.tripnn.data.database.usersettings.Theme
import ru.nn.tripnn.data.database.usersettings.UserSettings
import ru.nn.tripnn.data.datasource.usersettings.UserSettingsDataSource
import ru.nn.tripnn.data.request

class UserSettingsRepositoryImpl(
    private val userSettingsDataSource: UserSettingsDataSource
) : UserSettingsRepository {

    override fun getUserSettings(): Flow<Result<UserSettings?>> {
        return userSettingsDataSource.getUserSettings()
    }

    override suspend fun setTheme(theme: Theme): Result<Unit> = request {
        userSettingsDataSource.setTheme(theme)
    }

    override suspend fun setLanguage(language: Language): Result<Unit> = request {
        userSettingsDataSource.setLanguage(language)
    }

    override suspend fun setCurrency(currency: Currency): Result<Unit> = request {
        userSettingsDataSource.setCurrency(currency)
    }
}