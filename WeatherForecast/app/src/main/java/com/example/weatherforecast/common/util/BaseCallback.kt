package com.example.weatherforecast.common.util
import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.weatherforecast.R


open class BaseCallback(var context: Context?, var fragmentManager: FragmentManager?, var app: Application?):
    BaseEventListener {
    override fun changeStatusBarColor(colorId: Int) {
        if(colorId != 0) {
            val window = (context as Activity).window
            window.statusBarColor = ContextCompat.getColor(context!!, colorId)
        }
    }

    override fun showError(message: String?, title: String) {
        Toast.makeText(context, "$title\n$message", Toast.LENGTH_LONG).show()
    }

    override fun showNoInternet() {
        Toast.makeText(context, context?.getString(R.string.please_check_your_network), Toast.LENGTH_LONG).show()
    }
}
