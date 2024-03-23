package ru.nn.tripnn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.local.usersettings.UserSettingsDao
import ru.nn.tripnn.data.local.usersettings.UserSettingsDatabase
import ru.nn.tripnn.data.local.usersettings.UserSettingsRepositoryImpl
import ru.nn.tripnn.data.local.usersettings.UserSettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserSettingsModule {

    @Provides
    @Singleton
    fun userSettingsDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, UserSettingsDatabase::class.java, "user_settings.db")
            .build()

    @Provides
    @Singleton
    fun userSettingsDao(userSettingsDatabase: UserSettingsDatabase) =
        userSettingsDatabase.userSettingsDao()

    @Provides
    @Singleton
    fun userSettingsRepository(userSettingsDao: UserSettingsDao): UserSettingsRepository =
        UserSettingsRepositoryImpl(userSettingsDao)
}
