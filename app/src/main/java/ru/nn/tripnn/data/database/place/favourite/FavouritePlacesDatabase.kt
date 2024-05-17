package ru.nn.tripnn.data.database.place.favourite

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nn.tripnn.data.database.converter.ListConverters
import ru.nn.tripnn.data.database.route.favourite.FavouriteRoute

@Database(entities = [FavouriteRoute::class, FavouritePlace::class], version = 3, exportSchema = false)
@TypeConverters(ListConverters::class)
abstract class FavouritePlacesDatabase : RoomDatabase() {
    abstract fun favouritePlacesDao(): FavouritePlacesDao
}