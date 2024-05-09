package ru.nn.tripnn.data.local.history.place

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nn.tripnn.data.local.Converters

@Database(entities = [HistoryPlace::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PlacesHistoryDatabase : RoomDatabase() {
    abstract fun placesHistoryDao(): PlacesHistoryDao
}