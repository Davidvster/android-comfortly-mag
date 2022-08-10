package com.dv.comfortly.data.raw.sources.sensor

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface GpsSource {

    val locationClient: FusedLocationProviderClient

    val locationSettingsClient: SettingsClient

    class Default @Inject constructor(
        @ApplicationContext private val context: Context
    ) : GpsSource {

        override val locationClient: FusedLocationProviderClient
            get() = LocationServices.getFusedLocationProviderClient(context)

        override val locationSettingsClient: SettingsClient
            get() = LocationServices.getSettingsClient(context)
    }
}
