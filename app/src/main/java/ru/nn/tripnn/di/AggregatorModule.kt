package ru.nn.tripnn.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.datasource.favourite.FavouritesDataSource
import ru.nn.tripnn.data.datasource.history.HistoryDataSource
import ru.nn.tripnn.data.datasource.localroute.LocalRouteDataSource
import ru.nn.tripnn.data.datasource.placeinfo.PlaceInfoDataSource
import ru.nn.tripnn.data.datasource.remoteroute.RemoteRouteDataSource
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregatorImpl
import ru.nn.tripnn.data.repository.aggregator.RouteDataAggregator
import ru.nn.tripnn.data.repository.aggregator.RouteDataAggregatorImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AggregatorModule {
    @Provides
    @Singleton
    fun routeDataAggregator(
        placeDataAggregator: PlaceDataAggregator,
        localRouteDataSource: LocalRouteDataSource,
        remoteRouteDataSource: RemoteRouteDataSource,
        favouritesDataSource: FavouritesDataSource,
        historyDataSource: HistoryDataSource
    ): RouteDataAggregator = RouteDataAggregatorImpl(
        favouritesDataSource,
        historyDataSource,
        remoteRouteDataSource,
        localRouteDataSource,
        placeDataAggregator
    )

    @Provides
    @Singleton
    fun placeDataAggregator(
        favouritesDataSource: FavouritesDataSource,
        historyDataSource: HistoryDataSource,
        placeInfoDataSource: PlaceInfoDataSource
    ): PlaceDataAggregator =
        PlaceDataAggregatorImpl(favouritesDataSource, historyDataSource, placeInfoDataSource)
}