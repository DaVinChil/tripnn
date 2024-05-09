package ru.nn.tripnn.data.local.history

import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route

interface HistoryRepository {
    suspend fun getRoutes(): RemoteResource<List<Route>>
    suspend fun getPlaces(): RemoteResource<List<Place>>
    suspend fun saveRoute(route: Route)
    suspend fun savePlace(place: Place)
    suspend fun deleteRoute(route: Route)
    suspend fun deletePlace(place: Place)
    suspend fun clearPlacesHistory()
    suspend fun clearRoutesHistory()
    suspend fun hasRoute(route: Route): Boolean
    suspend fun hasPlace(place: Place): Boolean
}