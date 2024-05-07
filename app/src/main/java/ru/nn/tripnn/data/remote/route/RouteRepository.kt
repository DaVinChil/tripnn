package ru.nn.tripnn.data.remote.route

import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.data.RemoteResource

interface RouteRepository {
    suspend fun getRecommendations(): RemoteResource<List<Route>>
    suspend fun getFavourite(): RemoteResource<List<Route>>
    suspend fun removeFromFavourite(id: String)
    suspend fun addToFavourite(id: String)
}