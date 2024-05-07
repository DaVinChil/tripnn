package ru.nn.tripnn.domain

import java.util.Date

data class Route (
    val id: String,
    val name: String,
    val cost: String?,
    val desc: String?,
    val imageUrl: String?,
    val favourite: Boolean,
    val rating: Double?,
    val places: List<Place>,
    val wasTaken: Date?,
)