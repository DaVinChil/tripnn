package ru.nn.tripnn.data.datasource.distance

import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.api.DistanceApi
import ru.nn.tripnn.data.datasource.AbstractDataSource
import ru.nn.tripnn.data.dto.DistanceDto
import ru.nn.tripnn.data.getOrThrow

class DistanceDataSource(
    private val distanceApi: DistanceApi,
    ioDispatcher: CoroutineDispatcher
) : AbstractDataSource(ioDispatcher) {
    suspend fun distance(
        srcLon: Double,
        srcLat: Double,
        destLon: Double,
        destLat: Double
    ): Result<DistanceDto> = dispatchedRequest {
        distanceApi.getDistance(srcLon, srcLat, destLon, destLat).getOrThrow()
    }
}