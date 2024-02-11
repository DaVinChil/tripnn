package ru.nn.tripnn.data.local.preferences

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UiPreferencesDao {

    @Query("select * from ui_preferences limit 1")
    fun getPreferences(): UiPreferences

    @Insert(entity = UiPreferences::class)
    fun save(preferences: UiPreferences)
}