package ru.nn.tripnn.data.local.usersettings

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserSettings::class], version = 2, exportSchema = false)
abstract class UserSettingsDatabase : RoomDatabase() {
    abstract fun userSettingsDao(): UserSettingsDao
}