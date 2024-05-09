package ru.nn.tripnn.domain

import java.util.Date

data class Route (
    val id: String? = null,
    val name: String,
    val desc: String? = null,
    val imageUrl: String? = null,
    val favourite: Boolean,
    val rating: Double? = null,
    val places: List<Place> = listOf(),
    val wasTaken: Date? = null,
)