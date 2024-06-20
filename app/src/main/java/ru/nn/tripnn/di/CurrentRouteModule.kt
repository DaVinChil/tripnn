package ru.nn.tripnn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import ru.nn.tripnn.data.api.DistanceApi
import ru.nn.tripnn.data.database.currentroute.CurrentRouteDao
import ru.nn.tripnn.data.database.currentroute.CurrentRouteDatabase
import ru.nn.tripnn.data.datasource.currentroute.CurrentRouteDataSource
import ru.nn.tripnn.data.datasource.currentroute.CurrentRouteDataSourceImpl
import ru.nn.tripnn.data.datasource.distance.DistanceDataSource
import ru.nn.tripnn.data.datasource.favourite.FavouritesDataSource
import ru.nn.tripnn.data.datasource.localroute.LocalRouteDataSource
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.repository.currentroute.FakeCurrentRouteRepository
import ru.nn.tripnn.data.repository.routebuilder.FakeRouteBuilderServiceImpl
import ru.nn.tripnn.data.repository.routebuilder.RouteBuilderService
import ru.nn.tripnn.data.repository.routebuilder.RouteBuilderServiceImpl
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
    ): CurrentRouteDataSource = CurrentRouteDataSourceImpl(
        currentRouteDao,
        ioDispatcher
    )

    @Provides
    @Singleton
    fun distanceApi(retrofit: Retrofit) = retrofit.create(DistanceApi::class.java)

    @Provides
    @Singleton
    fun distanceDataSource(
        distanceApi: DistanceApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) = DistanceDataSource(distanceApi, ioDispatcher)

    @Provides
    @Singleton
//    @Fake
    fun routeBuilderService(distanceDataSource: DistanceDataSource): RouteBuilderService =
        FakeRouteBuilderServiceImpl()
//        RouteBuilderServiceImpl(distanceDataSource)

    @Provides
    @Singleton
    fun currentRouteRepository(
        currentRouteDataSource: CurrentRouteDataSource,
        localRouteDataSource: LocalRouteDataSource,
        favouritesDataSource: FavouritesDataSource,
        placeDataAggregator: PlaceDataAggregator,
        routeBuilderService: RouteBuilderService
    ): CurrentRouteRepository =
        CurrentRouteRepository(
            currentRouteDataSource,
            localRouteDataSource,
            favouritesDataSource,
            placeDataAggregator,
            routeBuilderService,
            CoroutineScope(Dispatchers.IO)
        )
}