package ru.nn.tripnn.data.database.place.favourite

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_places", indices = [Index(value = ["placeId"], unique = true)])
data class FavouritePlace (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val placeId: String
)