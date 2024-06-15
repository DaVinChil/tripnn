package ru.nn.tripnn.data.repository.routebuilder

import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.WalkInfo

interface RouteBuilderService {
    suspend fun walkInfo(place1: Place, place2: Place): Result<WalkInfo>
}