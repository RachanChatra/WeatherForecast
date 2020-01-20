package com.example.weatherforecast.common.util

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.weatherforecast.home.ui.HomeFragment


class NavigationController(var context: Context, var fragmentManager: FragmentManager){

    fun launchHomeFragment(){
        val fragment = HomeFragment()
        FragmentHelper.add(fragmentManager, android.R.id.content, fragment, fragment.javaClass.name)
    }
}
