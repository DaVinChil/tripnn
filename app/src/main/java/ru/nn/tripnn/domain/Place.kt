package ru.nn.tripnn.domain

import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
    val lonLatLocation: String? = null
) {
    fun isClosed(): Boolean {
        if (workTime == null || workTime == "24/7") return true

        val format = DateTimeFormatter.ofPattern("HH:mm")

        val rawFirstTime = workTime.substringBefore('-').trim()
        val rawSecondTime = workTime.substringAfter('-').trim()

        val firstTime = LocalTime.parse(rawFirstTime, format)
        val secondTime = LocalTime.parse(rawSecondTime, format)
        val now = LocalTime.now()

        return now.isBefore(firstTime) || now.isAfter(secondTime)
    }
}