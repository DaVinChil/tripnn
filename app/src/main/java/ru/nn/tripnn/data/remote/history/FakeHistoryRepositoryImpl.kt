package ru.nn.tripnn.data.remote.history

import kotlinx.coroutines.delay
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.data.stub_data.ROUTES
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.data.RemoteResource

class FakeHistoryRepositoryImpl : HistoryRepository {
    override suspend fun getRoutes(): RemoteResource<List<Route>> {
        delay(3000)
        return RemoteResource.Success(ROUTES)
    }

    override suspend fun getPlaces(): RemoteResource<List<Place>> {
        delay(3000)
        return RemoteResource.Success(listOf(PLACE_1))
    }
}