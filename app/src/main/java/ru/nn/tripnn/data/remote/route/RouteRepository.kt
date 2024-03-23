package ru.nn.tripnn.data.remote.route

import ru.nn.tripnn.domain.model.Route
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.domain.model.CurrentRoute

interface RouteRepository {
    suspend fun getRecommendations(): RemoteResource<List<Route>>
    suspend fun getFavourite(): RemoteResource<List<Route>>
    suspend fun removeFromFavourite(id: String)
    suspend fun addToFavourite(id: String)
}