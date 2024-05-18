package ru.nn.tripnn.data.datasource.usersettings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.nn.tripnn.data.database.usersettings.Currency
import ru.nn.tripnn.data.database.usersettings.Language
import ru.nn.tripnn.data.database.usersettings.Theme
import ru.nn.tripnn.data.database.usersettings.UserSettings
import ru.nn.tripnn.data.database.usersettings.UserSettingsDao
import ru.nn.tripnn.data.datasource.AbstractDataSource
import ru.nn.tripnn.data.toResultFlow

class UserSettingsDataSourceImpl(
    private val userSettingsDao: UserSettingsDao,
    ioDispatcher: CoroutineDispatcher
) : UserSettingsDataSource, AbstractDataSource(ioDispatcher) {
    override fun getUserSettings(): Flow<Result<UserSettings>> {
        return userSettingsDao.getUserSettings()
            .map { settings ->
                if (settings == null) {
                    createUserSettings()
                    UserSettings()
                } else {
                    settings
                }
            }.toResultFlow()
    }

    override suspend fun createUserSettings(): Result<Unit> = dispatchedRequest {
        userSettingsDao.createUserSettings(UserSettings())
    }

    override suspend fun setTheme(theme: Theme): Result<Unit> = dispatchedRequest {
        userSettingsDao.setTheme(theme)
    }

    override suspend fun setLanguage(language: Language): Result<Unit> = dispatchedRequest {
        userSettingsDao.setLanguage(language)
    }

    override suspend fun setCurrency(currency: Currency): Result<Unit> = dispatchedRequest {
        userSettingsDao.setCurrency(currency)
    }
}