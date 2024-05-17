package ru.nn.tripnn.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.datasource.recommendations.FakeRecommendationsDataSource
import ru.nn.tripnn.data.datasource.recommendations.RecommendationsDataSource
import ru.nn.tripnn.data.datasource.remoteroute.FakeRemoteRouteDataSource
import ru.nn.tripnn.data.datasource.remoteroute.RemoteRouteDataSource
import ru.nn.tripnn.data.repository.aggregator.RouteDataAggregator
import ru.nn.tripnn.data.repository.route.RouteRecommendationsRepository
import ru.nn.tripnn.data.repository.route.RouteRecommendationsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRouteModule {
    @Provides
    @Singleton
    @Fake
    fun recommendationDataSource(): RecommendationsDataSource = FakeRecommendationsDataSource()

    @Provides
    @Singleton
    @Fake
    fun remoteRouteDataSource(): RemoteRouteDataSource = FakeRemoteRouteDataSource()

    @Provides
    @Singleton
    fun recommendationsRepository(
        @Fake recommendationsDataSource: RecommendationsDataSource,
        routeDataAggregator: RouteDataAggregator
    ): RouteRecommendationsRepository =
        RouteRecommendationsRepositoryImpl(recommendationsDataSource, routeDataAggregator)
}