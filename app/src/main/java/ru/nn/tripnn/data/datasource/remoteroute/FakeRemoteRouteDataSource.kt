package ru.nn.tripnn.data.datasource.remoteroute

import ru.nn.tripnn.data.datasource.stubdata.dto.DTO_ROUTES
import ru.nn.tripnn.data.dto.RouteDto
import ru.nn.tripnn.data.request

class FakeRemoteRouteDataSource : RemoteRouteDataSource {
    private val routes = DTO_ROUTES

    override suspend fun findById(id: Long): Result<RouteDto> {
        val route = routes.find { it.id == id }

        if (route == null) return Result.failure(Exception())
        return Result.success(route)
    }

    override suspend fun findByIds(ids: List<Long>): Result<List<RouteDto>> = request {
        ids.map { findById(it).getOrThrow() }
    }
}