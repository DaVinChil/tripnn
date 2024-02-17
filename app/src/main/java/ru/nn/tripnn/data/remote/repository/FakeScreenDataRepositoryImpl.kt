package ru.nn.tripnn.data.remote.repository

import ru.nn.tripnn.data.stub_data.homeScreenData
import ru.nn.tripnn.domain.repository.ScreenDataRepository
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

class FakeScreenDataRepositoryImpl @Inject constructor() : ScreenDataRepository {
    override suspend fun getHomeScreenData() = Resource.Success(homeScreenData)
}