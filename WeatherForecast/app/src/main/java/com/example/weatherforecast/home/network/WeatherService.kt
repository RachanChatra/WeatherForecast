package com.example.weatherforecast.home.network

import com.example.myapplication.common.constants.ApiConstants
import com.example.weatherforecast.home.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service class to fetch current weather information
 */
interface WeatherService {

    @GET(ApiConstants.WEATHER_END_POINT)
    fun getCurrentWeatherInfoByCityId(@Query("id") id: Int, @Query("appid") appId: String): Call<WeatherResponse>

    @GET(ApiConstants.WEATHER_END_POINT)
    fun getCurrentWeatherInfoByLocation(@Query("lat") lat: String, @Query("lon") lng: String, @Query("appid") appId: String): Call<WeatherResponse>

}
