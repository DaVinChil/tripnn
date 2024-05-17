package ru.nn.tripnn.data.dto

import androidx.compose.runtime.Immutable
import ru.nn.tripnn.domain.Place

@Immutable
data class PlaceDto (
    val id: String,
    val name: String = "",
    val photos: List<String> = listOf(),
    val type: String? = null,
    val lon: Double,
    val lat: Double,
    val address: String? = null,
    val workTime: String? = null,
    val phone: String? = null,
    val cost: String? = null,
    val rating: Double = 0.0,
    val reviews: Int = 0,
    val doubleGisLink: String? = null,
) {
    fun toPlace(favourite: Boolean, visited: Boolean): Place {
        return Place(
            favourite = favourite,
            visited = visited,
            id = id,
            photos = photos,
            name = name,
            type = type,
            address = address,
            workTime = workTime,
            phone = phone,
            cost = cost,
            rating = rating,
            reviews = reviews
        )
    }
}