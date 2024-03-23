package ru.nn.tripnn.data.remote.place

import ru.nn.tripnn.domain.model.Place
import ru.nn.tripnn.domain.model.SearchFilters
import ru.nn.tripnn.data.RemoteResource

interface PlaceRepository {
    suspend fun find(searchState: SearchFilters): RemoteResource<List<Place>>
    suspend fun getFavourite(): RemoteResource<List<Place>>
    suspend fun removeFromFavourite(id: String)
    suspend fun addToFavourite(id: String)
    suspend fun findById(id: String): RemoteResource<Place>
}