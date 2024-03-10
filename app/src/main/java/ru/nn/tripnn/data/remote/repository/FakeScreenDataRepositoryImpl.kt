package ru.nn.tripnn.data.remote.repository

import kotlinx.coroutines.delay
import ru.nn.tripnn.data.stub_data.homeScreenData
import ru.nn.tripnn.domain.repository.ScreenDataRepository
import ru.nn.tripnn.domain.screen.HomeScreenData
import ru.nn.tripnn.domain.util.RemoteResource
import javax.inject.Inject

class FakeScreenDataRepositoryImpl @Inject constructor() : ScreenDataRepository {
    override suspend fun getHomeScreenData(): RemoteResource<HomeScreenData> {
        delay(3000)
        return RemoteResource.Success(homeScreenData)
    }
}