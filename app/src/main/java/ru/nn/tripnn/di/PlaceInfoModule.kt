package ru.nn.tripnn.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import ru.nn.tripnn.data.api.PlaceInfoApi
import ru.nn.tripnn.data.datasource.placeinfo.FakePlaceInfoDataSource
import ru.nn.tripnn.data.datasource.placeinfo.PlaceInfoDataSource
import ru.nn.tripnn.data.datasource.placeinfo.PlaceInfoDataSourceImpl
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.searchplace.SearchPlaceService
import ru.nn.tripnn.data.repository.searchplace.SearchPlaceServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaceInfoModule {

    @Provides
    @Singleton
    fun placeInfoApi(retrofit: Retrofit) = retrofit.create(PlaceInfoApi::class.java)

    @Provides
    @Singleton
//    @Fake
    fun placeInfoDataSource(
        placeInfoApi: PlaceInfoApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): PlaceInfoDataSource =
        PlaceInfoDataSourceImpl(placeInfoApi, ioDispatcher)
//        FakePlaceInfoDataSource()

    @Provides
    @Singleton
    fun searchPlaceService(
        placeInfoDataSource: PlaceInfoDataSource,
        placeDataAggregator: PlaceDataAggregator
    ): SearchPlaceService = SearchPlaceServiceImpl(placeInfoDataSource, placeDataAggregator)
}