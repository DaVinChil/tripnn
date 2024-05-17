package ru.nn.tripnn.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ListConverters {
    @TypeConverter
    fun listFromJson(json: String): List<String> {
        val listType = object : TypeToken<List<String?>?>() {}.type
        val res: List<String> = Gson().fromJson(json, listType)
        return res
    }

    @TypeConverter
    fun jsonFromList(list: List<String>): String {
        return Gson().toJson(list)
    }
}