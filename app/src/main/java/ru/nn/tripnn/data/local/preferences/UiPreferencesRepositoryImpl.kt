package ru.nn.tripnn.data.local.preferences

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nn.tripnn.domain.repository.UiPreferencesRepository
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

class UiPreferencesRepositoryImpl @Inject constructor(
    private val uiPreferencesDao: UiPreferencesDao
) : UiPreferencesRepository {

    override suspend fun getUiPreferences(): Resource<UiPreferences> {
        return withContext(Dispatchers.IO) {
            Resource.Success(uiPreferencesDao.getPreferences())
        }
    }


    override suspend fun saveUiPreferences(uiPreferences: UiPreferences) =
        withContext(Dispatchers.IO) {
            uiPreferencesDao.save(uiPreferences)
        }
}