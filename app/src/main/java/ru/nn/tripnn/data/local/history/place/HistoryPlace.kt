package ru.nn.tripnn.data.local.history.place

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "places_history", indices = [Index(value = ["placeId"], unique = true)])
data class HistoryPlace (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val placeId: String
)