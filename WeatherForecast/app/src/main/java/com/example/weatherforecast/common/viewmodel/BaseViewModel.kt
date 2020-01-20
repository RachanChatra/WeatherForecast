package com.example.weatherforecast.common.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity


open class BaseViewModel(val  app: Application): AndroidViewModel(app) {

    var headerLeftImage: Drawable? = null

    var headerRightImage: Drawable? = null

    var headerTitle: String = ""

    var showProgressView: Boolean = false

    private var hideHeaderRightButton: ObservableField<Boolean> = ObservableField(false)

    private var hideHeaderLeftButton: ObservableField<Boolean> = ObservableField(false)

    fun headerLeftImage(drawable: Drawable?) {
        headerLeftImage = drawable
        notifyChange()
    }

    fun headerRightImage(drawable: Drawable?) {
        headerRightImage = drawable
        notifyChange()
    }

    fun hideHeaderRightButton(hide: Boolean) {
        hideHeaderRightButton.set(hide)
        notifyChange()
    }

    fun getHideHeaderRightButton(): Boolean {
        return hideHeaderRightButton.get() ?:false
    }

    fun headerTitle(title: String){
        headerTitle = title
        notifyChange()
    }

    fun showProgressView(){
        showProgressView = true
        notifyChange()
    }

    fun hideProgressView(){
        showProgressView = false
        notifyChange()
    }

    /**
     * To get left button
     */
    fun getHideHeaderLeftButton(): Boolean {
        return hideHeaderLeftButton.get()?:false
    }

    fun hideHeaderLeftButton(hide: Boolean) {
        hideHeaderLeftButton.set(hide)
        notifyChange()
    }

    open fun onLeftViewClick(view: View) {
        popFragment(view.context)
    }

    /**
     * This method will pop the fragment from back stack
     */
    fun popFragment(context: Context) {
        val activity = context as FragmentActivity
        activity.onBackPressed()
    }

    fun finishActivity(context: Context){
        val activity = context as FragmentActivity
        activity.finish()
    }
    /**
     * Called when right view is clicked.
     */
    open fun onRightViewClick(view: View) {

    }

}
