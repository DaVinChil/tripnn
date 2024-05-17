package ru.nn.tripnn.data.database.place.history

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nn.tripnn.data.database.converter.ListConverters

@Database(entities = [VisitedPlace::class], version = 4, exportSchema = false)
@TypeConverters(ListConverters::class)
abstract class PlacesHistoryDatabase : RoomDatabase() {
    abstract fun placesHistoryDao(): PlacesHistoryDao
}