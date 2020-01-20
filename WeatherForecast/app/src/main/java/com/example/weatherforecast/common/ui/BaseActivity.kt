package com.example.myapplication.common.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.weatherforecast.common.util.BaseCallback
import com.example.weatherforecast.common.util.NavigationController


abstract class BaseActivity : FragmentActivity() {
    var navigationController: NavigationController? = null
    private var baseCallback: BaseCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        navigationController = NavigationController(this, supportFragmentManager)
    }

    /**
     * This method will return the implementation of Event Listeners.
     *
     * @param moduleType Module Type
     */
    fun getModuleCallback(moduleType: Int = ModuleType.NONE): BaseCallback {
        when (moduleType) {
            ModuleType.HOME_FRAGMENT -> {
                baseCallback = BaseCallback(this, supportFragmentManager, application)
            }
            else -> baseCallback = BaseCallback(this, supportFragmentManager, application)
        }
        return baseCallback as BaseCallback
    }

    interface ModuleType {
        companion object {
            const val NONE = -1
            const val HOME_FRAGMENT = 1
        }
    }
}
