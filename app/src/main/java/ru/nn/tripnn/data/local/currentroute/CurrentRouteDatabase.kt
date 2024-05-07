package ru.nn.tripnn.data.local.currentroute

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.nn.tripnn.data.local.Converters
import ru.nn.tripnn.domain.CurrentRoute

@Database(
    entities = [CurrentRoute::class], version = 2, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CurrentRouteDatabase : RoomDatabase() {
    abstract fun currentRouteDao(): CurrentRouteDao
}