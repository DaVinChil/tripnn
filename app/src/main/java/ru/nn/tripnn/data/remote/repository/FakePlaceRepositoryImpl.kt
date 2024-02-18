package ru.nn.tripnn.data.remote.repository

import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.data.stub_data.PLACE_FULL_1
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.PlaceFull
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.util.Resource
import ru.nn.tripnn.ui.screen.main.search.SearchState
import javax.inject.Inject

class FakePlaceRepositoryImpl @Inject constructor(

) : PlaceRepository {
    override suspend fun find(searchState: SearchState): Resource<List<Place>> {
        return Resource.Success(listOf(PLACE_1))
    }

    override suspend fun getFavourite(): Resource<List<Place>> {
        return Resource.Success(listOf(PLACE_1))
    }

    override suspend fun removeFromFavourite(id: String) {

    }
    override suspend fun addToFavourite(id: String) {

    }
    override suspend fun getFullInfo(id: String): Resource<PlaceFull> {
        return Resource.Success(PLACE_FULL_1)
    }
}