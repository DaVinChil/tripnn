package ru.nn.tripnn.data.repository.routebuilder

import ru.nn.tripnn.domain.Place

class FakeRouteBuilderServiceImpl : RouteBuilderService {
    override suspend fun timeToWalk(place1: Place, place2: Place) : Result<Int> {
        return Result.success(15)
    }
}