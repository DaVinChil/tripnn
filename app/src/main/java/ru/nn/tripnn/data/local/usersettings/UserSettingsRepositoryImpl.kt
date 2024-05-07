package ru.nn.tripnn.data.local.usersettings

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nn.tripnn.data.RemoteResource

class UserSettingsRepositoryImpl(
    private val userSettingsDao: UserSettingsDao
) : UserSettingsRepository {

    override suspend fun getUserSettings(): RemoteResource<UserSettings> {
        return withContext(Dispatchers.IO) {
            RemoteResource.Success(userSettingsDao.getUserSettings())
        }
    }

    override suspend fun saveUserSettings(userSettings: UserSettings) =
        withContext(Dispatchers.IO) {
            userSettingsDao.saveUserSettings(userSettings)
        }
}