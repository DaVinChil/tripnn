package ru.nn.tripnn.data.database.usersettings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val theme: Theme = Theme.LIGHT,
    val currency: Currency = Currency.RUB,
    val language: Language = Language.RUSSIAN,
)