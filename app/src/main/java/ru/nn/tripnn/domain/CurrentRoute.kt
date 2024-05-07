package ru.nn.tripnn.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_route")
data class CurrentRoute(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val places: List<Place> = listOf(),
    val currentPlaceIndex: Int? = null,
    val buildInProgress: Boolean = true,
    val routeId: String? = null
)
