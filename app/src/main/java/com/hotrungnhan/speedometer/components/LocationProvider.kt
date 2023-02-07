package com.hotrungnhan.speedometer.components

import android.annotation.SuppressLint
import android.location.*
import android.os.Debug
import android.os.Handler
import android.os.Looper
import android.util.Log
import org.koin.core.context.GlobalContext


class LocationProvider : LocationListener, GnssStatus.Callback() {

    private var locationChangeCallback: (Location) -> Unit = {}
    private var gpsSignalCallback: (signalStrength: Int) -> Unit = {}
    private var startTime = System.currentTimeMillis()
    private var currentGPSStrength = 1

    private val locationManager by lazy {
        GlobalContext.get().get<LocationManager>()
    }

    override fun onLocationChanged(location: Location) {
        Log.d("isvalid", isValidLocation(location).toString());
        Log.d("currentGPSStrength", currentGPSStrength.toString());
        Log.d("accuracy", location.accuracy.toString());
        if (isValidLocation(location)) {
            locationChangeCallback(location)
        }
    }

    @SuppressLint("MissingPermission")
    fun subscribe(
        locationChangeCallback: (Location) -> Unit,
        gpsSignalCallback: (gpsSignalStrength: Int) -> Unit
    ) {
        this.locationChangeCallback = locationChangeCallback
        this.gpsSignalCallback = gpsSignalCallback

        startTime = System.currentTimeMillis()
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0F, this)
        locationManager.registerGnssStatusCallback(
            this,
            Handler(Looper.getMainLooper())
        )
    }

    fun unsubscribe() {
        this.locationChangeCallback = {}
        locationManager.removeUpdates(this)
    }

    override fun onProviderEnabled(provider: String) {
        //Nothing to do
    }

    override fun onProviderDisabled(provider: String) {
        //Nothing to do
    }

    override fun onSatelliteStatusChanged(status: GnssStatus) {
        super.onSatelliteStatusChanged(status)
        status.let { gnsStatus ->
            val totalSatellites = gnsStatus.satelliteCount
            Log.d("ST_COUNT", gnsStatus.satelliteCount.toString())
            if (totalSatellites > 0) {
                var satellitesFixed = 0
                for (i in 0 until totalSatellites) {
                    if (gnsStatus.usedInFix(i)) {
                        satellitesFixed++
                    }
                }
                Log.d("ST_COUNT", satellitesFixed.toString())
                currentGPSStrength = (satellitesFixed * 100) / totalSatellites
                gpsSignalCallback(currentGPSStrength)
            }
        }
    }

    private fun isValidLocation(location: Location): Boolean {

        if (location.time < startTime) {
            return false
        }

//        if (currentGPSStrength == 0) {
//            return false
//        }

        if (location.accuracy <= 0 || location.accuracy > 20) {
            return false
        }
        return true
    }
}