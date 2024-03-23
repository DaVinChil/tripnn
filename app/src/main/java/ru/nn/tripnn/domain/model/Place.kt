package ru.nn.tripnn.domain.model

data class Place(
    val id: String,
    val photos: List<String>,
    val name: String,
    val type: String,
    val address: String,
    val workTime: String,
    val phone: String,
    val cost: String,
    val rating: Double,
    val reviews: Int,
    val doubleGisLink: String,
    val favourite: Boolean,
    val lonLatLocation: String
)