package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.screen.HomeScreenData
import ru.nn.tripnn.domain.util.RemoteResource

interface ScreenDataRepository {
    suspend fun getHomeScreenData(): RemoteResource<HomeScreenData>
}