package ru.nn.tripnn.data.repository.searchplace

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.nn.tripnn.data.datasource.placeinfo.FakePlaceInfoPagingDataSource
import ru.nn.tripnn.data.datasource.placeinfo.PlaceInfoPagingSource
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.SearchFilters
import ru.nn.tripnn.domain.state.ResState
import javax.inject.Inject

class FakeSearchPlaceService @Inject constructor() : SearchPlaceService {
    override fun find(
        searchState: SearchFilters,
        scope: CoroutineScope
    ): Pager<Int, StateFlow<ResState<Place>>> {
        return Pager(
            config = PagingConfig(pageSize = 1, enablePlaceholders = false),
            pagingSourceFactory = {
                FakePlaceInfoPagingDataSource(scope)
            }
        )
    }

    override fun findById(placeId: String): Flow<Result<Place>> {
        TODO("Not yet implemented")
    }
}