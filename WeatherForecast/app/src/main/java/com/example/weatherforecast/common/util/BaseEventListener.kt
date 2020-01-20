package com.example.weatherforecast.common.util

interface BaseEventListener {

    fun changeStatusBarColor(colorId: Int)

    fun showError(message: String?, title: String = "")

    fun showNoInternet()

}
