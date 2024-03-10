package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.util.RemoteResource

interface HistoryRepository {
    suspend fun getRoutes(): RemoteResource<List<Route>>
    suspend fun getPlaces(): RemoteResource<List<Place>>
}