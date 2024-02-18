package ru.nn.tripnn.domain.entity

data class Route(
    val id: String,
    val name: String,
    val cost: (String)?,
    val imageUrl: String,
    val favourite: Boolean,
    val rating: (Double)?
)