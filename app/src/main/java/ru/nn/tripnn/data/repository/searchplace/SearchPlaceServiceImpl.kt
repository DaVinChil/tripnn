package ru.nn.tripnn.data.repository.searchplace

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.nn.tripnn.data.datasource.placeinfo.PlaceInfoDataSource
import ru.nn.tripnn.data.datasource.placeinfo.PlaceInfoPagingSource
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.SearchFilters
import ru.nn.tripnn.domain.state.ResState
import ru.nn.tripnn.domain.state.toResStateFlow

class SearchPlaceServiceImpl(
    private val placeInfoDataSource: PlaceInfoDataSource,
    private val placeDataAggregator: PlaceDataAggregator
) : SearchPlaceService {

    override fun find(
        searchState: SearchFilters,
        scope: CoroutineScope
    ): Pager<Int, StateFlow<ResState<Place>>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                PlaceInfoPagingSource(placeInfoDataSource, searchState) {
                    placeDataAggregator.placeFromDto(it).toResStateFlow(scope)
                }
            }
        )
    }

    override fun findById(placeId: String): Flow<Result<Place>> {
        return placeDataAggregator.placeFromId(placeId)
    }
}