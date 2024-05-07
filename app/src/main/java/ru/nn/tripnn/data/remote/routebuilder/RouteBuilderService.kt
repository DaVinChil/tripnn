package ru.nn.tripnn.data.remote.routebuilder

import ru.nn.tripnn.domain.Place

interface RouteBuilderService {

    suspend fun timeToWalk(place1: Place, place2: Place): Int
}