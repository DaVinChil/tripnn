package ru.nn.tripnn.data.local.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.withTimeout
import ru.nn.tripnn.domain.repository.TokenRepository
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
val TOKEN_KEY = stringPreferencesKey("token_key")

class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext private val ctx: Context
) : TokenRepository {
    override suspend fun getToken(): String? {
        var token: String? = null
        delay(1000)
        try {
            withTimeout(3000) {
                ctx.dataStore.data.cancellable().collect { preferences ->
                    token = preferences[TOKEN_KEY]
                    currentCoroutineContext().cancel()
                }
            }
        } catch (ignored: Exception) {}

        return token
    }

    override suspend fun saveToken(token: String) {
        ctx.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun deleteToken() {
        ctx.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}