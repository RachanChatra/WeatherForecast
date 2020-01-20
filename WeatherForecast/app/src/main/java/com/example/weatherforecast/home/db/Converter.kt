package com.example.weatherforecast.home.db

import androidx.room.TypeConverter
import com.example.weatherforecast.home.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converter {

    @TypeConverter
    fun fromCoord(value: Coord?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun stringToCoord(value: String?): Coord? {
        return if (value == null)
            null
        else
            Gson().fromJson(value, Coord::class.java)
    }

    @TypeConverter
    fun fromMain(value: Main?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun stringToMain(value: String?): Main? {
        return if (value == null)
            null
        else
            Gson().fromJson(value, Main::class.java)
    }

    @TypeConverter
    fun fromWind(value: Wind?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun stringToWind(value: String?): Wind? {
        return if (value == null)
            null
        else
            Gson().fromJson(value, Wind::class.java)
    }

    @TypeConverter
    fun fromRain(value: Rain?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun stringToRain(value: String?): Rain? {
        return if (value == null)
            null
        else
            Gson().fromJson(value, Rain::class.java)
    }

    @TypeConverter
    fun fromClouds(value: Clouds?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun stringToClouds(value: String?): Clouds? {
        return if (value == null)
            null
        else
            Gson().fromJson(value, Clouds::class.java)
    }

    @TypeConverter
    fun fromSys(value: Sys?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun stringToSys(value: String?): Sys? {
        return if (value == null)
            null
        else
            Gson().fromJson(value, Sys::class.java)
    }

    @TypeConverter
    fun fromListOfWeather(value: List<Weather>?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun stringToListOfWeather(value: String?): List<Weather>? {
        val listType = object : TypeToken<ArrayList<Weather>>() {}.type
        return if (value == null)
            null
        else
            Gson().fromJson(value, listType)
    }
}
