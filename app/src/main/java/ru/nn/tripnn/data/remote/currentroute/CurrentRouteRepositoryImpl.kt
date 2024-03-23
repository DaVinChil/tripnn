package ru.nn.tripnn.data.remote.currentroute

import kotlinx.coroutines.delay
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.data.stub_data.ROUTE_1
import ru.nn.tripnn.domain.model.CurrentRoute

class CurrentRouteRepositoryImpl : CurrentRouteRepository {
    override suspend fun getCurrentRoute(): RemoteResource<CurrentRoute> {
        delay(2000)
        return RemoteResource.Success(CurrentRoute(route = ROUTE_1, currentPlaceIndex = 2))
    }
}