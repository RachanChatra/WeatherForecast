package com.example.weatherforecast.home.db

import android.content.Context
import androidx.room.*
import com.example.weatherforecast.home.model.WeatherResponse

/**
 *  Abstract class representing Weather Database
 */
@Database(entities = [WeatherResponse::class], version = 1)
@TypeConverters(Converter::class)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        const val DB_NAME = "weather_database"
        @Volatile
        private var database: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase {
            val tempInstance = database
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    DB_NAME
                ).build()
                database = instance
                return instance
            }
        }
    }
}
