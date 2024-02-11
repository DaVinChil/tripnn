package ru.nn.tripnn.data.local.preferences

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UiPreferences::class], version = 1, exportSchema = false)
abstract class UiPreferencesDatabase : RoomDatabase() {
    abstract fun uiPreferencesDao(): UiPreferencesDao
}