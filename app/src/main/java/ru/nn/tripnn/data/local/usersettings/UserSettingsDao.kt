package ru.nn.tripnn.data.local.usersettings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserSettingsDao {

    @Query("select * from user_settings limit 1")
    fun getUserSettings(): UserSettings

    @Insert(entity = UserSettings::class, onConflict = OnConflictStrategy.REPLACE)
    fun save(userSettings: UserSettings)
}