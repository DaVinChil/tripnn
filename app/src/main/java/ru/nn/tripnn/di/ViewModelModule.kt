package ru.nn.tripnn.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.local.token.TokenRepository
import ru.nn.tripnn.data.local.usersettings.UserSettingsRepository
import ru.nn.tripnn.data.remote.auth.AuthenticationService
import ru.nn.tripnn.ui.screen.authentication.AuthenticationViewModel
import ru.nn.tripnn.ui.screen.main.settings.UserSettingsViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    @Provides
    @Singleton
    fun userSettingsViewModel(repository: UserSettingsRepository) =
        UserSettingsViewModel(repository)

    @Provides
    @Singleton
    fun authenticationViewModel(@Fake authService: AuthenticationService, tokenRepository: TokenRepository) =
        AuthenticationViewModel(authService, tokenRepository)
}