package ru.nn.tripnn.data.database.route.localroute

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nn.tripnn.data.database.converter.ListConverters

@Database(entities = [LocalRoute::class], version = 2, exportSchema = false)
@TypeConverters(ListConverters::class)
abstract class LocalRouteDatabase : RoomDatabase() {
    abstract fun localRouteDao(): LocalRouteDao
}