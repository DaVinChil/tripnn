package ru.nn.tripnn.domain

import ru.nn.tripnn.data.database.route.RouteReference
import java.util.Date

data class Route (
    val localId: Long? = null,
    val remoteId: Long? = null,
    val title: String,
    val desc: String? = null,
    val imageUrl: String? = null,
    val favourite: Boolean,
    val rating: Double? = null,
    val places: List<Place> = listOf(),
    val wasTakenAt: Date? = null,
) : RouteReference {
    override fun localRouteId(): Long? = localId
    override fun remoteRouteId(): Long? = remoteId
}