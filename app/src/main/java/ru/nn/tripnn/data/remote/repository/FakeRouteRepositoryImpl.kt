package ru.nn.tripnn.data.remote.repository

import ru.nn.tripnn.data.stub_data.ROUTES
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.repository.RouteRepository
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

class FakeRouteRepositoryImpl @Inject constructor(

): RouteRepository {

    override suspend fun getRecommendations(): Resource<List<Route>> {
        return Resource.Success(ROUTES)
    }

    override suspend fun getFavourite(): Resource<List<Route>> {
        return Resource.Success(ROUTES)
    }

    override suspend fun removeFromFavourite(id: String) {

    }

    override suspend fun addToFavourite(id: String) {

    }
}