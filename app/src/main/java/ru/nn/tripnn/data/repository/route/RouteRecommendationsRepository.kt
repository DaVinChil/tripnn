package ru.nn.tripnn.data.repository.route

import kotlinx.coroutines.flow.Flow
import ru.nn.tripnn.domain.Route

interface RouteRecommendationsRepository {
    fun getRecommendations(): Flow<Result<List<Route>>>
    fun getRecommendationsByWord(word: String): Flow<Result<List<Route>>>
}