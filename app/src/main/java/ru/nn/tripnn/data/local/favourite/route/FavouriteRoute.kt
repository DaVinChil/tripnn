package ru.nn.tripnn.data.local.favourite.route

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import java.util.Date

@Entity(tableName = "favourite_routes")
data class FavouriteRoute (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val routeId: String?,
    val name: String,
    val desc: String?,
    val imageUrl: String?,
    val favourite: Boolean,
    val rating: Double?,
    val places: List<Place>,
    val wasTaken: Date?,
) {
    fun convertToRoute(): Route {
        return Route(
            localId = id,
            id = routeId,
            name = name,
            desc = desc,
            imageUrl = imageUrl,
            favourite = favourite,
            rating = rating,
            places = places,
            wasTaken = wasTaken
        )
    }

    companion object {
        fun fromRoute(route: Route): FavouriteRoute {
            return FavouriteRoute(
                id = route.localId,
                routeId = route.id,
                name = route.name,
                desc = route.desc,
                imageUrl = route.imageUrl,
                favourite = route.favourite,
                rating = route.rating,
                places = route.places,
                wasTaken = route.wasTaken
            )
        }
    }
}