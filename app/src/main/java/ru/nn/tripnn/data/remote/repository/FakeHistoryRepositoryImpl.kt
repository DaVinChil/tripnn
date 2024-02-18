package ru.nn.tripnn.data.remote.repository

import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.data.stub_data.ROUTES
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.repository.HistoryRepository
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

class FakeHistoryRepositoryImpl @Inject constructor(

) : HistoryRepository {
    override suspend fun getRoutes(): Resource<List<Route>> {
        return Resource.Success(ROUTES)
    }

    override suspend fun getPlaces(): Resource<List<Place>> {
        return Resource.Success(listOf(PLACE_1))
    }
}