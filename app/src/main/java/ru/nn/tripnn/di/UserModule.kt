package ru.nn.tripnn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.database.usersettings.UserSettingsDao
import ru.nn.tripnn.data.database.usersettings.UserSettingsDatabase
import ru.nn.tripnn.data.datasource.usersettings.UserSettingsDataSource
import ru.nn.tripnn.data.datasource.usersettings.UserSettingsDataSourceImpl
import ru.nn.tripnn.data.repository.userinfo.FakeUserInfoRepositoryImpl
import ru.nn.tripnn.data.repository.userinfo.UserInfoRepository
import ru.nn.tripnn.data.repository.usersettings.UserSettingsRepository
import ru.nn.tripnn.data.repository.usersettings.UserSettingsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    // User Info

    @Provides
    @Singleton
    @Fake
    fun userInfoRepository(): UserInfoRepository = FakeUserInfoRepositoryImpl()

    // UserSettings

    @Provides
    @Singleton
    fun userSettingsDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, UserSettingsDatabase::class.java, "user_settings.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun userSettingsDao(userSettingsDatabase: UserSettingsDatabase) =
        userSettingsDatabase.userSettingsDao()

    @Provides
    @Singleton
    fun userSettingsDataSource(
        userSettingsDao: UserSettingsDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): UserSettingsDataSource = UserSettingsDataSourceImpl(userSettingsDao, ioDispatcher)


    @Provides
    @Singleton
    fun userSettingsRepository(userSettingsDataSource: UserSettingsDataSource): UserSettingsRepository =
        UserSettingsRepositoryImpl(userSettingsDataSource)
}
