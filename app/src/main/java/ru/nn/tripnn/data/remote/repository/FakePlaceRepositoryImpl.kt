package ru.nn.tripnn.data.remote.repository

import kotlinx.coroutines.delay
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.SearchFilters
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.util.RemoteResource
import javax.inject.Inject

class FakePlaceRepositoryImpl @Inject constructor(

) : PlaceRepository {
    override suspend fun find(searchState: SearchFilters): RemoteResource<List<Place>> {
        delay(3000)
        return RemoteResource.Success(listOf(PLACE_1))
    }

    override suspend fun getFavourite(): RemoteResource<List<Place>> {
        delay(3000)
        return RemoteResource.Success(listOf(PLACE_1))
    }

    override suspend fun removeFromFavourite(id: String) {

    }

    override suspend fun addToFavourite(id: String) {

    }

    override suspend fun findById(id: String): RemoteResource<Place> {
        delay(2000)
        return RemoteResource.Success(PLACE_1)
    }
}