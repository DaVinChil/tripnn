package ru.nn.tripnn.data.repository.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.nn.tripnn.data.datasource.AbstractDataSource

class TokenRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    ioDispatcher: CoroutineDispatcher
) : TokenRepository, AbstractDataSource(ioDispatcher) {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("token_key")
    }

    override fun getToken(): Flow<Result<String?>> {
        return dataStore.data.map { prefs -> dispatchedRequest { prefs[TOKEN_KEY] } }
    }

    override suspend fun saveToken(token: String): Result<Unit> {
        return dispatchedRequest {
            dataStore.edit { userPreferences ->
                userPreferences[TOKEN_KEY] = token
            }
        }
    }

    override suspend fun deleteToken(): Result<Unit> {
        return dispatchedRequest {
            dataStore.edit { userPreferences ->
                userPreferences.clear()
            }
        }
    }
}