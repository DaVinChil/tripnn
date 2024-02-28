package ru.nn.tripnn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.local.preferences.UiPreferencesDao
import ru.nn.tripnn.data.local.preferences.UiPreferencesDatabase
import ru.nn.tripnn.data.local.preferences.UiPreferencesRepositoryImpl
import ru.nn.tripnn.domain.repository.UiPreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun uiPreferencesDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, UiPreferencesDatabase::class.java, "preferences.db")
            .build()

    @Provides
    @Singleton
    fun uiPreferencesDao(uiPreferencesDatabase: UiPreferencesDatabase) =
        uiPreferencesDatabase.uiPreferencesDao()

    @Provides
    @Singleton
    fun uiPreferencesRepository(uiPreferencesDao: UiPreferencesDao): UiPreferencesRepository =
        UiPreferencesRepositoryImpl(uiPreferencesDao)
}
