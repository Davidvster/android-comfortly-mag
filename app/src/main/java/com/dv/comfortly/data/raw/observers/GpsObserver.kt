package com.dv.comfortly.data.raw.observers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.dv.comfortly.data.raw.exceptions.GpsTurnedOffError
import com.dv.comfortly.data.raw.exceptions.NoLocationPermissionError
import com.dv.comfortly.data.raw.models.LocationData
import com.dv.comfortly.data.raw.sources.sensor.GpsSource
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationSettingsRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

interface GpsObserver : BaseObserver<LocationData> {

    companion object {
        val LOCATION_REQUEST_INTERVAL = 0.5.seconds
    }

    class Default @Inject constructor(
        @ApplicationContext private val context: Context,
        private val gpsSource: GpsSource,
    ) : GpsObserver {

        override fun observe(): Flow<LocationData> = callbackFlow {

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw NoLocationPermissionError()
            }

            val locationClient = gpsSource.locationClient
            val locationSettingsClient = gpsSource.locationSettingsClient

            val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_REQUEST_INTERVAL.inWholeMilliseconds)

            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val task = locationSettingsClient.checkLocationSettings(builder.build())

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    Timber.d("Got location data $locationResult")
                    locationResult.lastLocation?.let { location ->
                        this@callbackFlow.trySend(
                            LocationData(
                                location.latitude,
                                location.longitude,
                                location.altitude,
                                location.accuracy,
                                location.bearing,
                                location.bearingAccuracyDegrees,
                                location.speed,
                                location.speedAccuracyMetersPerSecond,
                            )
                        )
                    }
                }
            }

            task.addOnSuccessListener {
                locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }

            task.addOnFailureListener {
                throw GpsTurnedOffError()
            }

            awaitClose {
                locationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}
