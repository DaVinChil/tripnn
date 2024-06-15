package ru.nn.tripnn.data.repository.route

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.nn.tripnn.data.datasource.recommendations.RecommendationsDataSource
import ru.nn.tripnn.data.dto.RouteDto
import ru.nn.tripnn.data.repository.aggregator.RouteDataAggregator
import ru.nn.tripnn.data.toResultFlow
import ru.nn.tripnn.domain.Route

class RouteRecommendationsRepositoryImpl(
    private val recommendationsDataSource: RecommendationsDataSource,
    private val routeDataAggregator: RouteDataAggregator
) : RouteRecommendationsRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getRecommendations(): Flow<Result<List<Route>>> {
        val routesFlow: Flow<Result<List<RouteDto>>> = flow {
            emit(recommendationsDataSource.getRouteRecommendations())
        }

        return routesFlow.map { routeDataAggregator.routes(it.getOrThrow()) }.flattenConcat()
    }

    override fun getRecommendationsByWord(word: String): Flow<Result<List<Route>>> {
        return getRecommendations().map { it.getOrThrow().filter { route -> route.title.contains(word) } }
            .toResultFlow()
    }
}