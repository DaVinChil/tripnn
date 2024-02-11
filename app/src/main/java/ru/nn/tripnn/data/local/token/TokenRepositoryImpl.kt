package ru.nn.tripnn.data.local.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withTimeout
import ru.nn.tripnn.domain.repository.TokenRepository
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
val TOKEN_KEY = stringPreferencesKey("token_key")

class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext private val ctx: Context
) : TokenRepository{
    override suspend fun getToken(): String? {
        var token: String? = null
        try {
            withTimeout(3000) {
                ctx.dataStore.data.collect { preferences ->
                    token = preferences[TOKEN_KEY]
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