package ru.nn.tripnn.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.nn.tripnn.domain.Place


class Converters {

    @TypeConverter
    fun placesFromJson(json: String): List<Place> {
        val listType = object : TypeToken<List<String?>?>() {}.type
        val listIds: List<String> = Gson().fromJson(json, listType)

        val res = mutableListOf<Place>()
        listIds.forEach {
            res.add(Place(id = it))
        }

        return res
    }

    @TypeConverter
    fun jsonFromPlaces(places: List<Place>): String {
        val listIds = mutableListOf<String>().apply {
            places.forEach {
                add(it.id)
            }
        }

        return Gson().toJson(listIds)
    }

    @TypeConverter
    fun timeFromJson(json: String): List<Int> {
        val listType = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(json, listType)
    }

    @TypeConverter
    fun jsonFromTime(timeToWalk: List<Int>): String {
        return Gson().toJson(timeToWalk)
    }
}