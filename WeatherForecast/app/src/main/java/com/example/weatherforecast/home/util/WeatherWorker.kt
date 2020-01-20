package com.example.weatherforecast.home.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.common.constants.ApiConstants
import com.example.myapplication.common.network.RetrofitHelper
import com.example.myapplication.common.util.BundleConstants
import com.example.weatherforecast.common.constants.Constants
import com.example.weatherforecast.common.network.NetworkUtil
import com.example.weatherforecast.home.db.WeatherDatabase
import com.example.weatherforecast.home.db.WeatherRepository
import com.example.weatherforecast.home.model.WeatherResponse
import com.example.weatherforecast.home.network.WeatherService
import com.google.gson.Gson
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Response

import java.io.IOException

/**
 * Worker class that fetches current weather info based on the constraint set by WorkManager
 */
class WeatherWorker(val ctx: Context, params: WorkerParameters) : Worker(ctx, params),
    KoinComponent {
    var curLng: Double? = null
    var curLat: Double? = null
    override fun doWork(): Result {
        val weatherDatabase: WeatherDatabase by inject()
        val retrofitHelper: RetrofitHelper by inject()
        val weatherRepository: WeatherRepository? =
            WeatherRepository.getInstance(weatherDatabase.getWeatherDao())
        val (isNetworkAvailable, networkType) = NetworkUtil.isInternetAvailable(ctx)
        return if (isNetworkAvailable && networkType == NetworkUtil.NetworkType.WIFI) {
            val weatherService = retrofitHelper.getRetrofit()?.create(WeatherService::class.java)
            //val call: Call<WeatherResponse> = weatherService.getCurrentWeatherInfoByCityId(7778677, ApiConstants.WEATHER_API_ID)
            curLat = inputData.getDouble(BundleConstants.LATTITUDE, 0.0)
            curLng = inputData.getDouble(BundleConstants.LONGITUDE, 0.0)
            updateCurrentLocation()
            val call: Call<WeatherResponse>? = weatherService?.getCurrentWeatherInfoByLocation(
                curLat.toString(),
                curLng.toString(),
                ApiConstants.WEATHER_API_ID
            )
            val response: Response<WeatherResponse>? = call?.clone()?.execute()
            if (response != null)
                handleWeatherResponse(response, weatherRepository)
            else
                Result.failure()
        } else {
            Result.retry()
        }
    }

    /**
     * Method to handle [WeatherResponse]
     */
    private fun handleWeatherResponse(
        response: Response<WeatherResponse>,
        weatherRepository: WeatherRepository?
    ): Result {
        return try {
            if (response.code() == 200) {
                val weatherResponse = response.body()
                val responseStr = Gson().toJson(weatherResponse)
                val outputData =
                    Data.Builder().putString(Constants.WEATHER_RESPONSE, responseStr).build()
                weatherResponse?.let {
                    weatherRepository?.insert(it)
                }
                Result.success(outputData)
            } else {
                Result.retry()
            }
        } catch (e: IOException) {
            Result.failure()
        }
    }

    /**
     * Method to update current location
     */
    private fun updateCurrentLocation() {
        ctx.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // Register the listener with the Location Manager to receive location updates
            if (ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // If the user has removed location permission from system settings, Then return
                return
            }
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                curLat = location.latitude
                curLng = location.longitude
            }
        }

    }
}
