package ru.nn.tripnn.data.datasource.recommendations

import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.api.RecommendedRoutesApi
import ru.nn.tripnn.data.datasource.AbstractDataSource
import ru.nn.tripnn.data.dto.RouteDto
import ru.nn.tripnn.data.getOrThrow

class RecommendedRoutesDataSourceImpl (
    private val recommendedRoutesApi: RecommendedRoutesApi,
    ioDispatcher: CoroutineDispatcher
) : RecommendationsDataSource, AbstractDataSource(ioDispatcher) {
    override suspend fun getRouteRecommendations(): Result<List<RouteDto>> = dispatchedRequest {
        val routes = try {
            recommendedRoutesApi.getRoutes()
        } catch (e: Exception) {
            throw e
        }
        routes.getOrThrow()
    }
}