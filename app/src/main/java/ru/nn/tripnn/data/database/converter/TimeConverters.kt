package ru.nn.tripnn.data.database.converter

import androidx.room.TypeConverter
import java.util.Date

class TimeConverters {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(long: Long?): Date? {
        return long?.let { Date(it) }
    }
}