package ru.nn.tripnn.data.datasource.placeinfo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.StateFlow
import ru.nn.tripnn.data.dto.PlaceDto
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.SearchFilters
import ru.nn.tripnn.domain.state.ResState

class PlaceInfoPagingSource(
    private val placeInfoDataSource: PlaceInfoDataSource,
    private val searchFilters: SearchFilters,
    private val request: (PlaceDto) -> StateFlow<ResState<Place>>
) : PagingSource<Int, StateFlow<ResState<Place>>>() {

    override fun getRefreshKey(state: PagingState<Int, StateFlow<ResState<Place>>>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        return state.closestPageToPosition(anchorPosition)?.prevKey
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StateFlow<ResState<Place>>> {
        val data = placeInfoDataSource.find(searchFilters, params.key, params.loadSize)

        if (data.isFailure) {
            return LoadResult.Error(data.exceptionOrNull() ?: Exception())
        }

        val places = data.getOrThrow().map(request)

        return LoadResult.Page(
            data = places,
            prevKey = params.key?.minus(1),
            nextKey = if (places.isEmpty()) null else params.key?.plus(1) ?: 1
        )
    }
}