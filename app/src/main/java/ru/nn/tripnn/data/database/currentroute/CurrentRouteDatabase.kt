package ru.nn.tripnn.data.database.currentroute

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nn.tripnn.data.database.converter.ListConverters

@Database(entities = [CurrentRouteEntity::class], version = 4, exportSchema = false)
@TypeConverters(ListConverters::class)
abstract class CurrentRouteDatabase : RoomDatabase() {
    abstract fun currentRouteDao(): CurrentRouteDao
}