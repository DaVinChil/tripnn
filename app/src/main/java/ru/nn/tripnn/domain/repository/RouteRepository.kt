package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.util.Resource

interface RouteRepository {
    suspend fun getRecommendations(): Resource<List<Route>>
    suspend fun getFavourite(): Resource<List<Route>>
    suspend fun removeFromFavourite(id: String)
    suspend fun addToFavourite(id: String)
}