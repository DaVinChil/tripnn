package ru.nn.tripnn.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nn.tripnn.data.datasource.placeinfo.FakePlaceInfoDataSource
import ru.nn.tripnn.data.datasource.placeinfo.PlaceInfoDataSource
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.searchplace.SearchPlaceService
import ru.nn.tripnn.data.repository.searchplace.SearchPlaceServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaceInfoModule {

    @Provides
    @Singleton
    @Fake
    fun placeInfoDataSource(): PlaceInfoDataSource = FakePlaceInfoDataSource()

    @Provides
    @Singleton
    fun searchPlaceService(
        @Fake placeInfoDataSource: PlaceInfoDataSource,
        placeDataAggregator: PlaceDataAggregator
    ): SearchPlaceService = SearchPlaceServiceImpl(placeInfoDataSource, placeDataAggregator)
}