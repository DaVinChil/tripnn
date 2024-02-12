package ru.nn.tripnn.data.local.preferences

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ui_preferences")
data class UiPreferences(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val theme: Int,
    val currency: Int,
    val language: Int,
)