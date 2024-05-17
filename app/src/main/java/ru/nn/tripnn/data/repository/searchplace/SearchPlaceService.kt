package ru.nn.tripnn.data.repository.searchplace

import kotlinx.coroutines.flow.Flow
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.SearchFilters

interface SearchPlaceService {
    fun find(searchState: SearchFilters): Flow<Result<List<Place>>>
    fun findById(placeId: String): Flow<Result<Place>>
}