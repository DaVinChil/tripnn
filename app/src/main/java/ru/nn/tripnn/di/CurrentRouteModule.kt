package ru.nn.tripnn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.local.currentroute.CurrentRouteDao
import ru.nn.tripnn.data.local.currentroute.CurrentRouteDatabase
import ru.nn.tripnn.data.local.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.remote.place.PlaceRepository
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
object CurrentRouteModule {

    @Provides
    @Singleton
    fun currentRouteDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, CurrentRouteDatabase::class.java, "current_route.db")
            .build()

    @Provides
    @Singleton
    fun currentRouteDao(currentRouteDatabase: CurrentRouteDatabase) =
        currentRouteDatabase.currentRouteDao()

    @Provides
    @Singleton
    fun currentRouteRepository(currentRouteDao: CurrentRouteDao, @Fake placeRepository: PlaceRepository) =
        CurrentRouteRepository(currentRouteDao, placeRepository)
}