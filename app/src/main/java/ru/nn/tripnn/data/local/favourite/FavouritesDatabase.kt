package ru.nn.tripnn.data.local.favourite

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nn.tripnn.data.local.Converters
import ru.nn.tripnn.data.local.favourite.place.FavouritePlace
import ru.nn.tripnn.data.local.favourite.place.FavouritePlacesDao
import ru.nn.tripnn.data.local.favourite.route.FavouriteRoute
import ru.nn.tripnn.data.local.favourite.route.FavouriteRoutesDao

@Database(entities = [FavouriteRoute::class, FavouritePlace::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavouritesDatabase : RoomDatabase() {
    abstract fun favouriteRoutesDao(): FavouriteRoutesDao
    abstract fun favouritePlacesDao(): FavouritePlacesDao
}