package ru.nn.tripnn.data.repository.routebuilder

import ru.nn.tripnn.domain.Place

interface RouteBuilderService {
    suspend fun timeToWalk(place1: Place, place2: Place): Result<Int>
}