package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.util.Resource

interface HistoryRepository {
    suspend fun getRoutes(): Resource<List<Route>>
    suspend fun getPlaces(): Resource<List<Place>>
}