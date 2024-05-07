package ru.nn.tripnn.data.remote.history

import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.data.RemoteResource

interface HistoryRepository {
    suspend fun getRoutes(): RemoteResource<List<Route>>
    suspend fun getPlaces(): RemoteResource<List<Place>>
}