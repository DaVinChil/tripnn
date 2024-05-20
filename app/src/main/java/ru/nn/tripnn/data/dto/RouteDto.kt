package ru.nn.tripnn.data.dto

import ru.nn.tripnn.data.database.route.RouteReference
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import java.util.Date

data class RouteDto (
    val id: Long,
    val title: String,
    val desc: String,
    val image: String,
    val places: List<PlaceDto>
): RouteReference {
    fun toRoute(places: List<Place>, favourite: Boolean, wasTakenAt: Date?): Route {
        return Route(
            title = title,
            desc = desc,
            imageUrl = image,
            places = places,
            favourite = favourite,
            wasTakenAt = wasTakenAt,
            remoteId = id
        )
    }

    override fun localRouteId() = null

    override fun remoteRouteId() = id
}