package ru.nn.tripnn.data.repository.route

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nn.tripnn.data.datasource.stubdata.ui.ROUTES
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.Route
import javax.inject.Inject

class FakeRecommendationsRepository @Inject constructor() : RouteRecommendationsRepository {
    override fun getRecommendations(): Flow<Result<List<Route>>> {
        return flow { Result.success(ROUTES) }
    }

    override fun getRecommendationsByWord(word: String): Flow<Result<List<Route>>> {
        return flow { Result.success(ROUTES) }
    }

}