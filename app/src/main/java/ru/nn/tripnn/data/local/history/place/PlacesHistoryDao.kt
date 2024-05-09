package ru.nn.tripnn.data.local.history.place

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlacesHistoryDao {
    @Query("select * from places_history")
    fun getPlaces(): List<HistoryPlace>

    @Query("select count(*) from places_history where placeId = :placeId")
    fun hasByPlaceId(placeId: String): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun savePlace(historyPlace: HistoryPlace)

    @Delete
    fun deletePlace(historyPlace: HistoryPlace)

    @Query("delete from places_history")
    fun clearPlacesHistory()
}