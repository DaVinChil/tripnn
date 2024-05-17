package ru.nn.tripnn.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.nn.tripnn.data.repository.auth.AuthenticationService
import ru.nn.tripnn.data.repository.auth.FakeAuthenticationServiceImpl
import ru.nn.tripnn.data.repository.auth.TokenRepository
import ru.nn.tripnn.data.repository.auth.TokenRepositoryImpl
import javax.inject.Singleton

private const val DATA_STORE_NAME = "token"

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Fake
    @Provides
    @Singleton
    fun authenticationService(): AuthenticationService = FakeAuthenticationServiceImpl()

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(DATA_STORE_NAME) }
        )

    @Provides
    @Singleton
    fun tokenRepository(
        dataStore: DataStore<Preferences>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): TokenRepository = TokenRepositoryImpl(dataStore, ioDispatcher)
}