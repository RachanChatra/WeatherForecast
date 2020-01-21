package com.example.weatherforecast

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Data
import androidx.work.testing.TestWorkerBuilder
import com.example.myapplication.common.util.BundleConstants
import com.example.weatherforecast.common.network.NetworkUtil
import com.example.weatherforecast.home.util.WeatherWorker
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class WeatherWorkerTest {

    private lateinit var context: Context
    private lateinit var executor: Executor

    private val curLat = 13.22
    private val curLng = 12.11
    private val retry = "Retry"
    private val success = "Success"


    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        executor = Executors.newSingleThreadExecutor()
    }

    @Test
    fun shouldRetryIfWifiIsNotThere() {
        val inputData = Data.Builder()
            .putDouble(BundleConstants.LATTITUDE, curLat)
            .putDouble(BundleConstants.LONGITUDE, curLng)
            .build()
        val worker = TestWorkerBuilder<WeatherWorker>(
            context = context,
            executor = executor,
            inputData = inputData
        ).build()

        val result = worker.doWork()
        val (isNetworkAvailable, networkType) =  NetworkUtil.isInternetAvailable(context)
        if (isNetworkAvailable){
            when(networkType){
                NetworkUtil.NetworkType.CELLULAR -> {
                    assertNotNull(result)
                    assertTrue(result.toString().contains(retry))
                }
                NetworkUtil.NetworkType.WIFI -> {
                    assertNotNull(result)
                    assertTrue(result.toString().contains(success))

                }
            }
        }
    }
}