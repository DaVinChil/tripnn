package ru.nn.tripnn.domain

data class CurrentRoute(
    val places: List<Place> = listOf(),
    val timeToWalk: List<Int> = listOf(),
    val currentPlaceIndex: Int = 0,
    val buildInProgress: Boolean = true,
    val remoteRouteId: Long? = null
)