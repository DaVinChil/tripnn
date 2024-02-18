package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.PlaceFull
import ru.nn.tripnn.domain.util.Resource
import ru.nn.tripnn.ui.screen.main.search.SearchState

interface PlaceRepository {
    suspend fun find(searchState: SearchState): Resource<List<Place>>
    suspend fun getFavourite(): Resource<List<Place>>
    suspend fun getFullInfo(id: String): Resource<PlaceFull>
    suspend fun removeFromFavourite(id: String)
    suspend fun addToFavourite(id: String)
}