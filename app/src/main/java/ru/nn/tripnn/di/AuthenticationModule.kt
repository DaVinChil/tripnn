package ru.nn.tripnn.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.local.token.TokenRepositoryImpl
import ru.nn.tripnn.data.remote.auth.FakeAuthenticationServiceImpl
import ru.nn.tripnn.data.remote.auth.AuthenticationService
import ru.nn.tripnn.data.local.token.TokenRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Fake
    @Provides
    @Singleton
    fun authenticationService(): AuthenticationService = FakeAuthenticationServiceImpl()

    @Provides
    @Singleton
    fun tokenRepository(
        dataStore: DataStore<Preferences>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): TokenRepository = TokenRepositoryImpl(dataStore, ioDispatcher)
}