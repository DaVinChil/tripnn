package ru.nn.tripnn.data.database.place.history

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "places_history", indices = [Index(value = ["placeId"], unique = true)])
data class VisitedPlace (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val placeId: String
)