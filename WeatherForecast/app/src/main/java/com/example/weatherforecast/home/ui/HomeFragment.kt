package com.example.weatherforecast.home.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.common.ui.BaseActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.common.ui.BaseFragment
import com.example.weatherforecast.databinding.HomeFragmentBinding
import com.example.weatherforecast.home.model.WeatherResponse
import com.example.weatherforecast.home.util.HomeEventListener
import com.example.weatherforecast.home.viewmodel.HomeViewModel

const val MY_PERMISSIONS_REQUEST_LOCATION = 1
const val TWO_HOURS_IN_MILLI_SECONDS = 2 * 60 * 60 * 1000L

class HomeFragment : BaseFragment() {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var homeEventListener: HomeEventListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val parentView = super.onCreateView(inflater, container, savedInstanceState)
        binding = HomeFragmentBinding.inflate(inflater)
        homeEventListener =
            getModuleCallback(BaseActivity.ModuleType.HOME_FRAGMENT) as HomeEventListener
        setUpViewModel()
        initObservers()
        setContentView(binding.root, viewModel)
        return parentView
    }

    /**
     * Method to initialize live data observers
     */
    private fun initObservers() {
        val currentWeatherObserver: Observer<List<WeatherResponse>> = Observer {
            val weather = it?.firstOrNull()
            weather?.let {
                viewModel.handleResponse(weather)
            }
        }
        viewModel.currentWeatherLD?.observe(this, currentWeatherObserver)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (checkLocationPermission()) {
            getCurrentLocation()
        }
    }

    /**
     * Method to set up [HomeViewModel]
     */
    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.headerTitle = getString(R.string.current_weather)
        viewModel.setListener(homeEventListener)
        context?.let {
            viewModel.headerLeftImage(ContextCompat.getDrawable(it, R.drawable.ic_arrow))
        }
        binding.homeViewModel = viewModel
    }

    /**
     * Method to check if the app has location permission, If not present the user with permission dialog.
     * And if the user approves the permission schedule weather API call in [HomeViewModel]
     */
    private fun checkLocationPermission(): Boolean {
        context?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity as Activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    AlertDialog.Builder(context)
                        .setTitle(getString(R.string.enable_location_permission))
                        .setMessage("")
                        .setPositiveButton(
                            getString(R.string.ok)
                        ) { dialogInterface, i ->
                            //Prompt the user once explanation has been shown
                            this.requestPermissions(
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                MY_PERMISSIONS_REQUEST_LOCATION
                            )
                        }
                        .create()
                        .show()


                } else {
                    // No explanation needed, we can request the permission.
                    this.requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION
                    )
                }
                return false
            } else {
                return true
            }
        }
        return false
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                context?.let {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // location-related task you need to do.
                        if (ContextCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            getCurrentLocation()
                        }

                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return
                }
            }
        }
    }

    /**
     * Method to get current location
     */
    private fun getCurrentLocation() {
        context?.let {
            val locationManager = it.getSystemService(LOCATION_SERVICE) as LocationManager

            // Define a listener that responds to location updates

            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location?) {
                    viewModel.setCurrentLatLng(
                        location?.latitude ?: 0.0,
                        location?.longitude ?: 0.0
                    )
                    //viewModel.scheduleFetchWeatherAPI()
                }

                override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

                }

                override fun onProviderEnabled(p0: String?) {

                }

                override fun onProviderDisabled(p0: String?) {

                }

            }

            // Register the listener with the Location Manager to receive location updates
            if (ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener
            )
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                viewModel.scheduleFetchWeatherAPI()
            }
        }

    }
}
