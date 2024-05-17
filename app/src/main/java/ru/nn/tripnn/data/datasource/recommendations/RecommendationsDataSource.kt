package ru.nn.tripnn.data.datasource.recommendations

import ru.nn.tripnn.data.dto.RouteDto

interface RecommendationsDataSource {
    suspend fun getRouteRecommendations(): Result<List<RouteDto>>
}