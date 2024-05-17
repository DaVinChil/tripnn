package ru.nn.tripnn.data.database.route

interface RouteReference {
    fun localRouteId(): Long?
    fun remoteRouteId(): Long?
}