package ru.nn.tripnn.data.local.usersettings

import androidx.room.TypeConverter

class UserSettingsConverters {
    @TypeConverter
    fun fromTheme(theme: Theme) = theme.id

    @TypeConverter
    fun toTheme(id: Int) = Theme.fromId(id)

    @TypeConverter
    fun fromLanguage(language: Language) = language.id

    @TypeConverter
    fun toLanguage(id: Int) = Language.fromId(id)

    @TypeConverter
    fun fromCurrency(currency: Currency) = currency.id

    @TypeConverter
    fun toCurrency(id: Int) = Currency.fromId(id)
}