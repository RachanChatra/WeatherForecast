package com.example.weatherforecast.home.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.weatherforecast.home.model.WeatherResponse

/**
 * Data access object for Weather DB
 */
@Dao
interface WeatherDao {
    @Insert(onConflict = REPLACE)
    fun insertCurrentWeather(weather: WeatherResponse)

    @Query("Select * from CurrentWeather")
    fun getWeather(): LiveData<List<WeatherResponse>>
}
