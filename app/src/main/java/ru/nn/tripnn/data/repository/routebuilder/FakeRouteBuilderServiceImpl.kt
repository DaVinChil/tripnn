package ru.nn.tripnn.data.repository.routebuilder

import kotlinx.coroutines.delay
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.WalkInfo

class FakeRouteBuilderServiceImpl : RouteBuilderService {
    override suspend fun walkInfo(place1: Place, place2: Place) : Result<WalkInfo> {
        delay(1000)
        return Result.success(WalkInfo(15, 2300))
    }
}