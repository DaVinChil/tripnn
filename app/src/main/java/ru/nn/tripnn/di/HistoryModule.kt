package ru.nn.tripnn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.database.place.history.PlacesHistoryDao
import ru.nn.tripnn.data.database.place.history.PlacesHistoryDatabase
import ru.nn.tripnn.data.database.route.history.RoutesHistoryDao
import ru.nn.tripnn.data.database.route.history.RoutesHistoryDatabase
import ru.nn.tripnn.data.database.route.localroute.LocalRouteDao
import ru.nn.tripnn.data.datasource.history.HistoryDataSource
import ru.nn.tripnn.data.datasource.history.HistoryDataSourceImpl
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.aggregator.RouteDataAggregator
import ru.nn.tripnn.data.repository.history.HistoryRepository
import ru.nn.tripnn.data.repository.history.HistoryRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HistoryModule {

    // Place

    @Provides
    @Singleton
    fun placesHistoryDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, PlacesHistoryDatabase::class.java, "places_history")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun placesHistoryDao(placesHistoryDatabase: PlacesHistoryDatabase) =
        placesHistoryDatabase.placesHistoryDao()

    // Route

    @Provides
    @Singleton
    fun routesHistoryDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, RoutesHistoryDatabase::class.java, "routes_history")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun routesHistoryDao(routesHistoryDatabase: RoutesHistoryDatabase) =
        routesHistoryDatabase.routesHistoryDao()

    // Data Source

    @Provides
    @Singleton
    fun historyDataSource(
        placesHistoryDao: PlacesHistoryDao,
        routesHistoryDao: RoutesHistoryDao,
        localRouteDao: LocalRouteDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): HistoryDataSource = HistoryDataSourceImpl(placesHistoryDao, routesHistoryDao, localRouteDao, ioDispatcher)

    // Repository

    @Provides
    @Singleton
    fun historyRepository(
        historyDataSource: HistoryDataSource,
        placeDataAggregator: PlaceDataAggregator,
        routeDataAggregator: RouteDataAggregator
    ): HistoryRepository = HistoryRepositoryImpl(historyDataSource, routeDataAggregator, placeDataAggregator)
}