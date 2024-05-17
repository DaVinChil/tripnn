package ru.nn.tripnn.domain

data class Place(
    val id: String,
    val photos: List<String> = listOf(),
    val name: String = "",
    val type: String? = null,
    val address: String? = null,
    val workTime: String? = null,
    val phone: String? = null,
    val cost: String? = null,
    val rating: Double = 0.0,
    val reviews: Int = 0,
    val doubleGisLink: String = "",
    val favourite: Boolean = false,
    val visited: Boolean = false,
    val lonLatLocation: String? = null,
    val timeToGo: Int? = null
)