package ru.nn.tripnn.data.remote.routebuilder

import kotlinx.coroutines.delay
import ru.nn.tripnn.domain.Place

class FakeRouteBuilderServiceImpl : RouteBuilderService {
    override suspend fun timeToWalk(place1: Place, place2: Place) : Int {
        delay(1000)
        return 15
    }
}