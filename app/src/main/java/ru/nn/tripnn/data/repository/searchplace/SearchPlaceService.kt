package ru.nn.tripnn.data.repository.searchplace

import androidx.paging.Pager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.SearchFilters
import ru.nn.tripnn.domain.state.ResState

interface SearchPlaceService {
    fun find(searchState: SearchFilters, scope: CoroutineScope): Pager<Int, StateFlow<ResState<Place>>>
    fun findById(placeId: String): Flow<Result<Place>>
}