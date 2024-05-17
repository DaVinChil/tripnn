package ru.nn.tripnn.data.database.usersettings

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nn.tripnn.data.database.converter.UserSettingsConverters

@Database(entities = [UserSettings::class], version = 3, exportSchema = false)
@TypeConverters(UserSettingsConverters::class)
abstract class UserSettingsDatabase : RoomDatabase() {
    abstract fun userSettingsDao(): UserSettingsDao
}