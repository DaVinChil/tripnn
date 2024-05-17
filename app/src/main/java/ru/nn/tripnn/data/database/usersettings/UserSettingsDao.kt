package ru.nn.tripnn.data.database.usersettings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    @Query("select * from user_settings limit 1")
    fun getUserSettings(): Flow<UserSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUserSettings(userSettings: UserSettings)

    @Query("update user_settings set theme = :theme")
    fun setTheme(theme: Theme)

    @Query("update user_settings set language = :language")
    fun setLanguage(language: Language)

    @Query("update user_settings set currency = :currency")
    fun setCurrency(currency: Currency)
}