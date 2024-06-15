package ru.nn.tripnn.domain

import ru.nn.tripnn.data.database.route.RouteReference

data class CurrentRoute(
    val places: List<Place> = listOf(),
    val walkInfo: List<WalkInfo> = listOf(),
    val currentPlaceIndex: Int = 0,
    val buildInProgress: Boolean = true,
    val remoteRouteId: Long? = null,
    val localRouteId: Long? = null,
    val favourite: Boolean = false,
    val finished: Boolean = false
) : RouteReference {
    override fun localRouteId(): Long? {
        return localRouteId
    }

    override fun remoteRouteId(): Long? {
        return remoteRouteId
    }

    val currentPlace get() = places[currentPlaceIndex]
}

data class WalkInfo(
    val timeToWalk: Int,
    val distance: Int
)