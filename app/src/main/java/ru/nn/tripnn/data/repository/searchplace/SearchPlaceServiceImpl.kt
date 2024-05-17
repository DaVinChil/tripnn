package ru.nn.tripnn.data.repository.searchplace

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import ru.nn.tripnn.data.datasource.placeinfo.PlaceInfoDataSource
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.SearchFilters

class SearchPlaceServiceImpl(
    private val placeInfoDataSource: PlaceInfoDataSource,
    private val placeDataAggregator: PlaceDataAggregator
): SearchPlaceService {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun find(searchState: SearchFilters): Flow<Result<List<Place>>> {
        val placeDtosFlow = flow { emit(placeInfoDataSource.find(searchState)) }

        return placeDtosFlow
            .flatMapLatest { placeDataAggregator.placesFromDtos(it.getOrThrow()) }
            .catch { e -> Result.failure<List<Place>>(e) }
    }

    override fun findById(placeId: String): Flow<Result<Place>> {
        return placeDataAggregator.placeFromId(placeId)
    }
}