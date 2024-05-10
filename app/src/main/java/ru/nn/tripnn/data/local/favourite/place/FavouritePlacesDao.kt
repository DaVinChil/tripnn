package ru.nn.tripnn.data.local.favourite.place

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouritePlacesDao {
    @Query("select * from favourite_places")
    fun getPlaces(): List<FavouritePlace>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePlace(favouritePlace: FavouritePlace)

    @Query("select * from favourite_places where placeId = :placeId")
    fun findByPlaceId(placeId: String): FavouritePlace?

    @Delete
    fun deleteByRouteId(placeId: String)

    @Query("delete from favourite_places")
    fun deleteAllFavouritePlaces()
}