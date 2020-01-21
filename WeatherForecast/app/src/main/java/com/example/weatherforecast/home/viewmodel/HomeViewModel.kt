package com.example.weatherforecast.home.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.myapplication.common.util.BundleConstants
import com.example.weatherforecast.R
import com.example.weatherforecast.common.network.NetworkUtil
import com.example.weatherforecast.common.viewmodel.BaseViewModel
import com.example.weatherforecast.home.db.WeatherDatabase
import com.example.weatherforecast.home.db.WeatherRepository
import com.example.weatherforecast.home.model.WeatherResponse
import com.example.weatherforecast.home.util.HomeEventListener
import com.example.weatherforecast.home.util.WeatherWorker
import java.util.concurrent.TimeUnit

const val WEATHER_WORKER = "WeatherWorker"

/**
 * View model class for home screen
 */
class HomeViewModel(app: Application) : BaseViewModel(app) {

    private var workManager: WorkManager = WorkManager.getInstance(app)
    private var weatherRepository: WeatherRepository?
    var city: ObservableField<String> = ObservableField()
    var weather: ObservableField<String> = ObservableField()
    var sunrise: ObservableField<String> = ObservableField()
    var sunset: ObservableField<String> = ObservableField()
    var temperature: ObservableField<String> = ObservableField()
    var country: ObservableField<String> = ObservableField()
    var latLng: ObservableField<String> = ObservableField()
    private lateinit var homeListener: HomeEventListener
    private var curLat: Double = 0.0
    private var curLng: Double = 0.0

    var currentWeatherLD: LiveData<List<WeatherResponse>>?

    init {
        val weatherDao = WeatherDatabase.getDatabase(app).getWeatherDao()
        weatherRepository = WeatherRepository.getInstance(weatherDao)
        currentWeatherLD = weatherRepository?.currentWeather
    }

    /**
     * Method to schedule Weather API via WorkManager
     */
    fun scheduleFetchWeatherAPI() {
        val (isNetworkAvailable, networkType) = NetworkUtil.isInternetAvailable(app)
        if (isNetworkAvailable && networkType == NetworkUtil.NetworkType.WIFI){
            showProgressView()
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val inputData = Data.Builder()
                .putDouble(BundleConstants.LATTITUDE, curLat)
                .putDouble(BundleConstants.LONGITUDE, curLng)
                .build()

            val periodicRequest: PeriodicWorkRequest =
                PeriodicWorkRequestBuilder<WeatherWorker>(15, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .setInputData(inputData)
                    .build()
            workManager.enqueueUniquePeriodicWork(
                WEATHER_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
        } else {
            homeListener.showNoInternet()
        }
    }

    /**
     * Method to set current lattitude and longitude
     * @param latitude
     * @param longitude
     */
    fun setCurrentLatLng(latitude: Double, longitude: Double) {
        curLat = latitude
        curLng = longitude
    }

    /**
     * Method to set home event listener
     * @param listener [HomeEventListener]
     */
    fun setListener(listener: HomeEventListener) {
        homeListener = listener
    }

    /**
     * Method to handle weather response
     * @param response [WeatherResponse]
     */
    fun handleResponse(response: WeatherResponse) {
        hideProgressView()
        city.set(response.name)
        val weatherObject = response.weather?.firstOrNull()
        weather.set(weatherObject?.description ?: "")
        sunrise.set(
            String.format(
                "%s %s",
                app.getString(R.string.sunrise_at),
                response.sys?.sunrise?.toString()
            )
        )
        sunset.set(
            String.format(
                "%s %s",
                app.getString(R.string.sunset_at),
                response.sys?.sunset?.toString()
            )
        )
        temperature.set(
            String.format(
                app.getString(R.string.temperature),
                response.main?.temp
            )
        )

        country.set(
            String.format(
                "%s %s", app.getString(R.string.country),
                response.sys?.country
            )
        )
        latLng.set(
            String.format(
                "%s (%s , %s)",
                app.getString(R.string.lat_lng),
                response.coord?.lat?.toString(),
                response.coord?.lon?.toString()
            )
        )
    }
}
