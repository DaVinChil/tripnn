package ru.nn.tripnn.data.remote.repository

import kotlinx.coroutines.delay
import ru.nn.tripnn.data.stub_data.homeScreenData
import ru.nn.tripnn.domain.repository.ScreenDataRepository
import ru.nn.tripnn.domain.screen.HomeScreenData
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

class FakeScreenDataRepositoryImpl @Inject constructor() : ScreenDataRepository {
    override suspend fun getHomeScreenData(): Resource<HomeScreenData> {
        delay(3000)
        return Resource.Success(homeScreenData)
    }
}