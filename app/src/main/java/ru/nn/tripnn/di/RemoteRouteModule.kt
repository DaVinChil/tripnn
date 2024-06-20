package ru.nn.tripnn.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import ru.nn.tripnn.data.api.RecommendedRoutesApi
import ru.nn.tripnn.data.api.RouteInfoApi
import ru.nn.tripnn.data.datasource.recommendations.FakeRecommendationsDataSource
import ru.nn.tripnn.data.datasource.recommendations.RecommendationsDataSource
import ru.nn.tripnn.data.datasource.recommendations.RecommendedRoutesDataSourceImpl
import ru.nn.tripnn.data.datasource.remoteroute.FakeRemoteRouteDataSource
import ru.nn.tripnn.data.datasource.remoteroute.RemoteRouteDataSource
import ru.nn.tripnn.data.datasource.remoteroute.RemoteRouteDataSourceImpl
import ru.nn.tripnn.data.repository.aggregator.RouteDataAggregator
import ru.nn.tripnn.data.repository.route.FakeRecommendationsRepository
import ru.nn.tripnn.data.repository.route.RouteRecommendationsRepository
import ru.nn.tripnn.data.repository.route.RouteRecommendationsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRouteModule {

    @Provides
    @Singleton
    fun recommendedApi(retrofit: Retrofit) = retrofit.create(RecommendedRoutesApi::class.java)

    @Provides
    @Singleton
//    @Fake
    fun recommendationDataSource(
        recommendedRoutesApi: RecommendedRoutesApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): RecommendationsDataSource =
        RecommendedRoutesDataSourceImpl(recommendedRoutesApi, ioDispatcher)
//        FakeRecommendationsDataSource()

    @Provides
    @Singleton
    fun remoteRouteInfoApi(retrofit: Retrofit) = retrofit.create(RouteInfoApi::class.java)

    @Provides
    @Singleton
//    @Fake
    fun remoteRouteDataSource(
        remoteRoutesApi: RouteInfoApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): RemoteRouteDataSource =
        RemoteRouteDataSourceImpl(remoteRoutesApi, ioDispatcher)
//        FakeRemoteRouteDataSource()

    @Provides
    @Singleton
    fun recommendationsRepository(
        recommendationsDataSource: RecommendationsDataSource,
        routeDataAggregator: RouteDataAggregator
    ): RouteRecommendationsRepository =
        RouteRecommendationsRepositoryImpl(recommendationsDataSource, routeDataAggregator)
//        FakeRecommendationsRepository()
}