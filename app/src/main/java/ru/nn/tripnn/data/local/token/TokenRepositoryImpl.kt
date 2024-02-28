package ru.nn.tripnn.data.local.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.nn.tripnn.domain.repository.TokenRepository
import javax.inject.Inject

val TOKEN_KEY = stringPreferencesKey("token_key")

class TokenRepositoryImpl (
    private val dataStore: DataStore<Preferences>,
    private val ioDispatcher: CoroutineDispatcher
) : TokenRepository {
    override suspend fun getToken(): String? =
        withContext(ioDispatcher) {
            dataStore.data.first()[TOKEN_KEY]
        }

    override suspend fun saveToken(token: String) {
        withContext(ioDispatcher) {
            dataStore.edit { userPreferences ->
                userPreferences[TOKEN_KEY] = token
            }
        }
    }

    override suspend fun deleteToken() {
        withContext(ioDispatcher) {
            dataStore.edit { userPreferences ->
                userPreferences.clear()
            }
        }
    }
}