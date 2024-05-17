package ru.nn.tripnn.data.datasource.recommendations

import ru.nn.tripnn.data.datasource.stubdata.dto.DTO_ROUTES
import ru.nn.tripnn.data.dto.RouteDto

class FakeRecommendationsDataSource : RecommendationsDataSource {
    override suspend fun getRouteRecommendations(): Result<List<RouteDto>> {
        return Result.success(DTO_ROUTES)
    }
}