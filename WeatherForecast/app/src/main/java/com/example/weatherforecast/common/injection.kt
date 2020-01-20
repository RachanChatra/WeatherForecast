package com.example.weatherforecast.common

import androidx.room.Room
import com.example.myapplication.common.network.RetrofitHelper
import com.example.weatherforecast.home.db.WeatherDao
import com.example.weatherforecast.home.db.WeatherDatabase
import com.example.weatherforecast.home.db.WeatherRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module(override = true) {
    factory { WeatherRepository(get())}

    single {
        Room.databaseBuilder(androidContext(),
            WeatherDatabase::class.java, WeatherDatabase.DB_NAME).build()
    }

    single {
        RetrofitHelper()
    }

}
