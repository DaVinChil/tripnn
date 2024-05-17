package ru.nn.tripnn.data.database.route.localroute

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import java.util.Date

@Entity(tableName = "local_routes")
data class LocalRoute (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val desc: String? = null,
    val rating: Double? = null,
    val imageUrl: String? = null,
    val placeIds: List<String>,
) {
    fun toRoute(favourite: Boolean, wasTakenAt: Date?, places: List<Place>): Route {
        return Route(
            localId = id,
            favourite = favourite,
            places = places,
            desc = desc,
            title = name,
            rating = rating,
            imageUrl = imageUrl,
            wasTakenAt = wasTakenAt
        )
    }
}