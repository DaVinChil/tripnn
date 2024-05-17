package ru.nn.tripnn.data.datasource.history

import ru.nn.tripnn.data.database.place.history.VisitedPlace
import ru.nn.tripnn.data.database.route.RouteReference
import ru.nn.tripnn.data.database.route.history.TakenRoute
import java.time.Instant
import java.util.Date


fun takenRoute(route: RouteReference): TakenRoute {
    return TakenRoute(
        localId = route.localRouteId(),
        remoteId = route.remoteRouteId(),
        wasTakenAt = Date.from(Instant.now())
    )
}

fun visitedPlace(id: String): VisitedPlace {
    return VisitedPlace(placeId = id)
}