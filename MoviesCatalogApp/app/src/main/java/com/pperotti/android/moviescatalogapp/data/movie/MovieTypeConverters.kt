package com.pperotti.android.moviescatalogapp.data.movie

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieTypeConverters {
    @TypeConverter
    fun fromStringToListInt(value: String?): List<Int>? {
        if (value == null) {
            return null
        }
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListIntToString(list: List<Int>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(list)
    }
}
