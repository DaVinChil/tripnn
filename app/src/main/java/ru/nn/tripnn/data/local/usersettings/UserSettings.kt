package ru.nn.tripnn.data.local.usersettings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val theme: Theme,
    val currency: Currency,
    val language: Language,
)