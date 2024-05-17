package ru.nn.tripnn.data.database.currentroute

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_route")
data class CurrentRouteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val places: List<String> = listOf(),
    val currentPlaceIndex: Int = 0,
    val buildInProgress: Boolean = true,
    val remoteRouteId: Long? = null
)
