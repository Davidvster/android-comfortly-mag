package com.dv.comfortly.data.raw.interactors

import android.content.IntentSender
import androidx.activity.result.IntentSenderRequest
import com.dv.comfortly.data.raw.observers.GpsObserver
import com.dv.comfortly.data.raw.sources.sensor.GpsSource
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

interface TurnOnGpsInteractor {
    suspend fun turnOnGps(): Pair<Boolean, IntentSenderRequest?>

    class Default
        @Inject
        constructor(
            private val gpsSource: GpsSource,
        ) : TurnOnGpsInteractor {
            override suspend fun turnOnGps(): Pair<Boolean, IntentSenderRequest?> =
                suspendCancellableCoroutine { continuation ->

                    val locationRequest =
                        LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(GpsObserver.LOCATION_REQUEST_INTERVAL.inWholeMilliseconds)

                    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                    val task = gpsSource.locationSettingsClient.checkLocationSettings(builder.build())

                    task.addOnFailureListener { exception ->
                        if (exception is ResolvableApiException) {
                            // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                            try {
                                val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                                // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                                if (continuation.isActive) {
                                    continuation.resume(false to intentSenderRequest)
                                }
                            } catch (sendEx: IntentSender.SendIntentException) {
                                // Ignore the error.
                            }
                        }
                    }

                    task.addOnSuccessListener {
                        if (continuation.isActive) {
                            continuation.resume(true to null)
                        }
                    }
                }
        }
}
