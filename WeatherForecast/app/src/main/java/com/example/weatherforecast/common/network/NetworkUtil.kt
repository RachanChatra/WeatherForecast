package com.example.weatherforecast.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


object NetworkUtil {
    fun isInternetAvailable(context: Context): Pair<Boolean, Int> {
        var result = false
        var networkType: Int = NetworkType.NONE
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                     when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            result = true
                            networkType = NetworkType.WIFI

                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            result = true
                            networkType = NetworkType.CELLULAR
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            result = true
                            networkType = NetworkType.ETHERNET
                        }
                        else -> {
                            result = false
                            networkType = NetworkType.NONE
                        }
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                        networkType = NetworkType.WIFI
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                        networkType = NetworkType.CELLULAR
                    }
                }
            }
        }
        return Pair(result, networkType)
    }

    interface NetworkType{
        companion object{
            const val WIFI = 1
            const val CELLULAR = 2
            const val ETHERNET = 3
            const val NONE = 4
        }
    }
}