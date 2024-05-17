package ru.nn.tripnn.data.repository.aggregator

import kotlinx.coroutines.flow.Flow
import ru.nn.tripnn.data.database.route.RouteReference
import ru.nn.tripnn.domain.Route
import java.util.Date

interface RouteDataAggregator {
    suspend fun routes(
        routes: List<RouteReference>,
        favourite: Boolean? = null,
        wasTakenAt: List<Date>? = null
    ): Flow<Result<List<Route>>>
}