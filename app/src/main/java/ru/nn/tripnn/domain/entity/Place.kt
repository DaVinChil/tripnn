package ru.nn.tripnn.domain.entity


data class Place(
    val imageUrl: String,
    val id: String,
    val rating: Double,
    val name: String,
    val type: String,
    val cost: String
)