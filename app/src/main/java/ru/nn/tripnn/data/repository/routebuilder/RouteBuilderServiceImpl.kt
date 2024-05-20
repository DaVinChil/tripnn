package ru.nn.tripnn.data.repository.routebuilder

import ru.nn.tripnn.data.datasource.distance.DistanceDataSource
import ru.nn.tripnn.domain.Place

class RouteBuilderServiceImpl(
    private val distanceDataSource: DistanceDataSource
) : RouteBuilderService {
    override suspend fun timeToWalk(place1: Place, place2: Place): Result<Int> {
        if (place1.lon == null || place1.lat == null || place2.lon == null || place2.lat == null){
            return Result.success(0)
        }
        return distanceDataSource.distance(place1.lon, place1.lat, place2.lon, place2.lat)
    }
}