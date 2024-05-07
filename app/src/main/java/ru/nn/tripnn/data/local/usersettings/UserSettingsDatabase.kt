package ru.nn.tripnn.data.local.usersettings

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserSettings::class], version = 3, exportSchema = false)
@TypeConverters(UserSettingsConverters::class)
abstract class UserSettingsDatabase : RoomDatabase() {
    abstract fun userSettingsDao(): UserSettingsDao
}