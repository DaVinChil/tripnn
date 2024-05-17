package ru.nn.tripnn.data.database.converter

import androidx.room.TypeConverter
import ru.nn.tripnn.data.database.usersettings.Currency
import ru.nn.tripnn.data.database.usersettings.Language
import ru.nn.tripnn.data.database.usersettings.Theme

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