package ru.nn.tripnn.data.database.route.history

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nn.tripnn.data.database.converter.TimeConverters

@Database(entities = [TakenRoute::class], version = 3, exportSchema = false)
@TypeConverters(TimeConverters::class)
abstract class RoutesHistoryDatabase : RoomDatabase() {
    abstract fun routesHistoryDao(): RoutesHistoryDao
}