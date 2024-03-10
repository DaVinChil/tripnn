package ru.nn.tripnn.data.local.preferences

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nn.tripnn.domain.repository.UiPreferencesRepository
import ru.nn.tripnn.domain.util.RemoteResource

class UiPreferencesRepositoryImpl(
    private val uiPreferencesDao: UiPreferencesDao
) : UiPreferencesRepository {

    override suspend fun getUiPreferences(): RemoteResource<UiPreferences> {
        return withContext(Dispatchers.IO) {
            RemoteResource.Success(uiPreferencesDao.getPreferences())
        }
    }


    override suspend fun saveUiPreferences(uiPreferences: UiPreferences) =
        withContext(Dispatchers.IO) {
            uiPreferencesDao.save(uiPreferences)
        }
}