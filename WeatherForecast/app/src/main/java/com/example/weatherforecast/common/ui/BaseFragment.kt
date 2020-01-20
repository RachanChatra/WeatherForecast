package com.example.weatherforecast.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.common.ui.BaseActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.common.util.BaseCallback
import com.example.weatherforecast.common.viewmodel.BaseViewModel
import com.example.weatherforecast.databinding.CommonBaseFragmentBinding
import com.example.weatherforecast.home.util.HomeCallback

abstract class BaseFragment : Fragment() {

    private lateinit var baseViewModel: BaseViewModel
    private lateinit var binding: CommonBaseFragmentBinding
    private var baseCallback: BaseCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.common_base_fragment, container, false)
        return binding.root
    }

    protected fun setContentView(view: View, viewModel: BaseViewModel) {
        binding.contentView.addView(view)
        initViewModel(viewModel)
    }

    /**
     * Method to initialize base view models
     */
    private fun initViewModel(viewModel: BaseViewModel?) {
        baseViewModel = viewModel ?: ViewModelProviders.of(this).get(BaseViewModel::class.java)
        binding.baseViewModel = baseViewModel
    }

    /**
     * This method will return the implementation of Event Listeners.
     *
     * @param moduleType Module Type
     */
    fun getModuleCallback(moduleType: Int = BaseActivity.ModuleType.NONE): BaseCallback {
        when (moduleType) {
            BaseActivity.ModuleType.HOME_FRAGMENT -> {
                baseCallback =
                    HomeCallback(activity, activity?.supportFragmentManager, activity?.application)
            }

            else -> baseCallback =
                BaseCallback(activity, activity?.supportFragmentManager, activity?.application)
        }
        return baseCallback as BaseCallback
    }
}
