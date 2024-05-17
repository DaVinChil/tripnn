package ru.nn.tripnn.data.datasource.remoteroute

import ru.nn.tripnn.data.dto.RouteDto

interface RemoteRouteDataSource {
    suspend fun findById(id: Long): Result<RouteDto>
    suspend fun findByIds(ids: List<Long>): Result<List<RouteDto>>
}