package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.SearchFilters
import ru.nn.tripnn.domain.util.Resource

interface PlaceRepository {
    suspend fun find(searchState: SearchFilters): Resource<List<Place>>
    suspend fun getFavourite(): Resource<List<Place>>
    suspend fun removeFromFavourite(id: String)
    suspend fun addToFavourite(id: String)
}