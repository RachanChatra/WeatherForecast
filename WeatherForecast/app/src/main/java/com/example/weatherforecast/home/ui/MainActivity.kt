package com.example.weatherforecast.home.ui

import android.os.Bundle
import com.example.myapplication.common.ui.BaseActivity
import com.example.weatherforecast.R

/**
 * Launcher activity
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    /**
     * Method to launch home fragment
     */
    private fun initialize() {
        navigationController?.launchHomeFragment()
    }
}
