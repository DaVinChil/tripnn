package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.util.RemoteResource

interface RouteRepository {
    suspend fun getRecommendations(): RemoteResource<List<Route>>
    suspend fun getFavourite(): RemoteResource<List<Route>>
    suspend fun removeFromFavourite(id: String)
    suspend fun addToFavourite(id: String)
}