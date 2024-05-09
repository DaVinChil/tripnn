package ru.nn.tripnn.data.local.history.route

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nn.tripnn.data.local.Converters

@Database(entities = [HistoryRoute::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RoutesHistoryDatabase : RoomDatabase() {
    abstract fun routesHistoryDao(): RoutesHistoryDao
}