package ru.nn.tripnn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.database.currentroute.CurrentRouteDao
import ru.nn.tripnn.data.database.currentroute.CurrentRouteDatabase
import ru.nn.tripnn.data.datasource.currentroute.CurrentRouteDataSource
import ru.nn.tripnn.data.datasource.currentroute.CurrentRouteDataSourceImpl
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.repository.routebuilder.FakeRouteBuilderServiceImpl
import ru.nn.tripnn.data.repository.routebuilder.RouteBuilderService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CurrentRouteModule {

    @Provides
    @Singleton
    fun currentRouteDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, CurrentRouteDatabase::class.java, "current_route.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun currentRouteDao(currentRouteDatabase: CurrentRouteDatabase) =
        currentRouteDatabase.currentRouteDao()

    @Provides
    @Singleton
    fun currentRouteDataSource(
        currentRouteDao: CurrentRouteDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CurrentRouteDataSource = CurrentRouteDataSourceImpl(currentRouteDao, ioDispatcher)

    @Provides
    @Singleton
    @Fake
    fun routeBuilderService(): RouteBuilderService = FakeRouteBuilderServiceImpl()

    @Provides
    @Singleton
    fun currentRouteRepository(
        currentRouteDataSource: CurrentRouteDataSource,
        placeDataAggregator: PlaceDataAggregator,
        @Fake routeBuilderService: RouteBuilderService
    ): CurrentRouteRepository = CurrentRouteRepository(currentRouteDataSource, placeDataAggregator, routeBuilderService)
}