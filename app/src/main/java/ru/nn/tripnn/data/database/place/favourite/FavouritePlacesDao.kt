package ru.nn.tripnn.data.database.place.favourite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritePlacesDao {
    @Query("select * from favourite_places")
    fun getPlaces(): Flow<List<FavouritePlace>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePlace(favouritePlace: FavouritePlace): Long

    @Query("select * from favourite_places where placeId = :placeId limit 1")
    fun findByPlaceId(placeId: String): Flow<FavouritePlace?>

    @Query("delete from favourite_places where placeId = :placeId")
    fun deleteByPlaceId(placeId: String)

    @Query("delete from favourite_places")
    fun deleteAllFavouritePlaces()
}