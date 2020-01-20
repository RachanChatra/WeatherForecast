package com.example.weatherforecast.home.db

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.weatherforecast.home.model.WeatherResponse

class WeatherRepository(val weatherDao: WeatherDao) {

    val currentWeather: LiveData<List<WeatherResponse>> = weatherDao.getWeather()


    fun insert(weatherResponse: WeatherResponse){
        //weatherDao.insertCurrentWeather(weatherResponse)
        DBAsyncTask(weatherResponse).execute()
    }

    companion object{
        @Volatile
        private var repo: WeatherRepository? = null

        fun getInstance(weatherDao: WeatherDao) : WeatherRepository? {
            synchronized(this) {
                if (repo == null){
                    repo = WeatherRepository(weatherDao)
                }
            }
            return repo
        }

    }

    inner class DBAsyncTask(val weatherResponse: WeatherResponse): AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg params: Unit?) {
            weatherDao.insertCurrentWeather(weatherResponse)
        }
    }
}
