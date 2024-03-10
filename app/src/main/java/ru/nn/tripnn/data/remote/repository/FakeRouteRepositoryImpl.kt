package ru.nn.tripnn.data.remote.repository

import kotlinx.coroutines.delay
import ru.nn.tripnn.data.stub_data.ROUTES
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.repository.RouteRepository
import ru.nn.tripnn.domain.util.RemoteResource
import javax.inject.Inject

class FakeRouteRepositoryImpl @Inject constructor(

): RouteRepository {

    override suspend fun getRecommendations(): RemoteResource<List<Route>> {
        delay(3000)
        return RemoteResource.Success(ROUTES)
    }

    override suspend fun getFavourite(): RemoteResource<List<Route>> {
        delay(3000)
        return RemoteResource.Success(ROUTES)
    }

    override suspend fun removeFromFavourite(id: String) {

    }

    override suspend fun addToFavourite(id: String) {

    }
}