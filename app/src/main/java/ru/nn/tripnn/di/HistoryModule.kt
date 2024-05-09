package ru.nn.tripnn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.local.history.HistoryRepository
import ru.nn.tripnn.data.local.history.HistoryRepositoryImpl
import ru.nn.tripnn.data.local.history.place.PlacesHistoryDao
import ru.nn.tripnn.data.local.history.place.PlacesHistoryDatabase
import ru.nn.tripnn.data.local.history.route.RoutesHistoryDao
import ru.nn.tripnn.data.local.history.route.RoutesHistoryDatabase
import ru.nn.tripnn.data.remote.place.PlaceRepository
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
object HistoryModule {

    @Provides
    @Singleton
    fun placesHistoryDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, PlacesHistoryDatabase::class.java, "places_history")
            .build()

    @Provides
    @Singleton
    fun placesHistoryDao(placesHistoryDatabase: PlacesHistoryDatabase) =
        placesHistoryDatabase.placesHistoryDao()

    @Provides
    @Singleton
    fun routesHistoryDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, RoutesHistoryDatabase::class.java, "routes_history")
            .build()

    @Provides
    @Singleton
    fun routesHistoryDao(routesHistoryDatabase: RoutesHistoryDatabase) =
        routesHistoryDatabase.routesHistoryDao()

    @Provides
    @Singleton
    fun historyRepository(
        routesHistoryDao: RoutesHistoryDao,
        placesHistoryDao: PlacesHistoryDao,
        @Fake placeRepository: PlaceRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): HistoryRepository = HistoryRepositoryImpl(
        routesHistoryDao,
        placesHistoryDao,
        placeRepository,
        ioDispatcher
    )
}