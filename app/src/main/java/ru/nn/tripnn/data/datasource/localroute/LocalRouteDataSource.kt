package ru.nn.tripnn.data.datasource.localroute

import ru.nn.tripnn.data.database.route.localroute.LocalRoute

interface LocalRouteDataSource {
    suspend fun findById(id: Long): Result<LocalRoute>
    suspend fun findByIds(ids: List<Long>): Result<List<LocalRoute>>
}