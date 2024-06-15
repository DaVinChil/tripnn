package ru.nn.tripnn.data.datasource.placeinfo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.nn.tripnn.data.datasource.stubdata.ui.PLACES
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.state.ResState

class FakePlaceInfoPagingDataSource(
    private val scope: CoroutineScope
) : PagingSource<Int, StateFlow<ResState<Place>>>() {
    override fun getRefreshKey(state: PagingState<Int, StateFlow<ResState<Place>>>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StateFlow<ResState<Place>>> {
        return LoadResult.Page(
            data = PLACES.map {
                flow<ResState<Place>> { ResState.Success(it) }
                    .stateIn(scope = scope)
            },
            prevKey = null,
            nextKey = null
        );
    }
}