package ru.nn.tripnn.data.datasource.remoteroute

import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.api.RouteInfoApi
import ru.nn.tripnn.data.datasource.AbstractDataSource
import ru.nn.tripnn.data.dto.RouteDto
import ru.nn.tripnn.data.getOrThrow

class RemoteRouteDataSourceImpl (
    private val routeInfoApi: RouteInfoApi,
    ioDispatcher: CoroutineDispatcher
) : RemoteRouteDataSource, AbstractDataSource(ioDispatcher) {
    override suspend fun findById(id: Long): Result<RouteDto> = dispatchedRequest {
        routeInfoApi.searchRouteById(id).getOrThrow()
    }
}