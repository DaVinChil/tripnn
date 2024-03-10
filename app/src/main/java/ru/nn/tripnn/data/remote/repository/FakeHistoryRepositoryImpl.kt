package ru.nn.tripnn.data.remote.repository

import kotlinx.coroutines.delay
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.data.stub_data.ROUTES
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.repository.HistoryRepository
import ru.nn.tripnn.domain.util.RemoteResource
import javax.inject.Inject

class FakeHistoryRepositoryImpl @Inject constructor(

) : HistoryRepository {
    override suspend fun getRoutes(): RemoteResource<List<Route>> {
        delay(3000)
        return RemoteResource.Success(ROUTES)
    }

    override suspend fun getPlaces(): RemoteResource<List<Place>> {
        delay(3000)
        return RemoteResource.Success(listOf(PLACE_1))
    }
}