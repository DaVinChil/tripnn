package ru.nn.tripnn.data.datasource.remoteroute

import ru.nn.tripnn.data.datasource.stubdata.dto.DTO_ROUTES
import ru.nn.tripnn.data.dto.RouteDto

class FakeRemoteRouteDataSource : RemoteRouteDataSource {
    private val routes = DTO_ROUTES

    override suspend fun findById(id: Long): Result<RouteDto> {
        val route = routes.find { it.id == id }

        if (route == null) return Result.failure(Exception())
        return Result.success(route)
    }
}