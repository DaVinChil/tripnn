package ru.nn.tripnn.data.database.route.favourite

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nn.tripnn.data.database.converter.ListConverters

@Database(entities = [FavouriteRoute::class], version = 3, exportSchema = false)
@TypeConverters(ListConverters::class)
abstract class FavouriteRoutesDatabase : RoomDatabase() {
    abstract fun favouriteRoutesDao(): FavouriteRoutesDao
}