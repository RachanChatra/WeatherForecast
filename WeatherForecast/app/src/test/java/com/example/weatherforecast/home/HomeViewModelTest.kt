package com.example.weatherforecast.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.weatherforecast.MockResponse
import com.example.weatherforecast.R
import com.example.weatherforecast.common.network.NetworkUtil
import com.example.weatherforecast.home.model.WeatherResponse
import com.example.weatherforecast.home.util.HomeEventListener
import com.example.weatherforecast.home.viewmodel.HomeViewModel
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(WorkManager::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var listener: HomeEventListener
    @Mock
    private lateinit var application: Application
    @Mock
    private lateinit var context: Context
    @Before
    fun setUp(){
        initMocks()
        viewModel = HomeViewModel(application)
        viewModel.setListener(listener)
        viewModel.setCurrentLatLng(0.0, 0.0)
    }

    private fun initMocks() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.mockStatic(WorkManager::class.java)
        val workManager = PowerMockito.mock(WorkManager::class.java)
        Mockito.`when`(WorkManager.getInstance(application)).thenReturn(workManager)
        Mockito.`when`(application.applicationContext).thenReturn(context)
        val ld = MediatorLiveData<WorkInfo>()
        Mockito.`when`(workManager.getWorkInfoByIdLiveData(any())).thenReturn(ld)
        Mockito.`when`(application.getString(R.string.sunrise_at)).thenReturn("Sunrise at :")
        Mockito.`when`(application.getString(R.string.sunset_at)).thenReturn("Sunset at :")
        Mockito.`when`(application.getString(R.string.country)).thenReturn("Country :")
        Mockito.`when`(application.getString(R.string.temperature)).thenReturn("Temperature : %s F")
        Mockito.`when`(application.getString(R.string.lat_lng)).thenReturn("Latitude and Longitude : ")

    }

    @Test
    fun `should set weather properties upon getting response`(){
        val weatherResponse = Gson().fromJson<WeatherResponse>(MockResponse.WEATHER_RESPONSE, WeatherResponse::class.java)
        viewModel.handleResponse(weatherResponse)
        assertEquals("Dublin City", viewModel.city.get())
        assertEquals("scattered clouds", viewModel.weather.get())
        assertEquals("Sunrise at : 1579422530", viewModel.sunrise.get())
        assertEquals("Sunset at : 1579452110", viewModel.sunset.get())
        assertEquals("Temperature : 272.79 F", viewModel.temperature.get())
        assertEquals("Country : IE", viewModel.country.get())
    }

    @Test
    fun `should show a network error message if internet is not there`(){
        Mockito.`when`(NetworkUtil.isInternetAvailable(context)).thenReturn(Pair(true, 1))
        viewModel.scheduleFetchWeatherAPI()
        verify(listener)?.showNoInternet()
    }
}
