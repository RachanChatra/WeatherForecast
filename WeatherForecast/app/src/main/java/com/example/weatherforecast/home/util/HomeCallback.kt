package com.example.weatherforecast.home.util

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.weatherforecast.common.util.BaseCallback

/**
 * Callback class for home screen
 */
class HomeCallback(context: Context?, fragmentManager: FragmentManager?, app: Application?) :
    BaseCallback(context, fragmentManager, app), HomeEventListener {
}
