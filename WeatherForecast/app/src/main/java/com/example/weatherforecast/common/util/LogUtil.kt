package com.example.weatherforecast.common.util

import android.util.Log
import com.example.weatherforecast.BuildConfig

class LogUtils {

    companion object {

        private val LOG_ENABLED = BuildConfig.DEBUG
        private const val TAG = "Weather Forecast"

        fun v(message: String, tag: String = TAG, throwable: Throwable? = null) {
            if (LOG_ENABLED)
                Log.v(tag, message, throwable)
        }

        fun d(message: String, tag: String = TAG, throwable: Throwable? = null) {
            if (LOG_ENABLED)
                Log.d(tag, message, throwable)
        }

        fun i(message: String, tag: String = TAG, throwable: Throwable? = null) {
            if (LOG_ENABLED)
                Log.i(tag, message, throwable)
        }

        fun e(message: String, tag: String = TAG, throwable: Throwable? = null) {
            if (LOG_ENABLED)
                Log.e(tag, message, throwable)
        }
    }
}
