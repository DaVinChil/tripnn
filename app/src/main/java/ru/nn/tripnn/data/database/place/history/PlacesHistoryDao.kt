package ru.nn.tripnn.data.database.place.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesHistoryDao {
    @Query("select * from places_history")
    fun getPlaces(): Flow<List<VisitedPlace>>

    @Query("select * from places_history where placeId = :placeId limit 1")
    fun findPlaceByPlaceId(placeId: String): Flow<VisitedPlace?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun savePlace(visitedPlace: VisitedPlace)

    @Query("delete from places_history where placeId = :placeId")
    fun deletePlace(placeId: String)

    @Query("delete from places_history")
    fun clearPlacesHistory()
}