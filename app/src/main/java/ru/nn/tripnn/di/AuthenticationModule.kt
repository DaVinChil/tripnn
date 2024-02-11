package ru.nn.tripnn.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.local.token.TokenRepositoryImpl
import ru.nn.tripnn.data.remote.FakeAuthenticationServiceImpl
import ru.nn.tripnn.domain.repository.AuthenticationService
import ru.nn.tripnn.domain.repository.TokenRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationModule {
    @Binds
    @Fake
    @Singleton
    abstract fun authenticationService(
        authenticationServiceImpl: FakeAuthenticationServiceImpl
    ): AuthenticationService

    @Binds
    @Singleton
    abstract fun tokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository
}