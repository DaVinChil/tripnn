package ru.nn.tripnn.data.remote.currentroute

import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.domain.model.CurrentRoute

interface CurrentRouteRepository {
    suspend fun getCurrentRoute(): RemoteResource<CurrentRoute>
}