package com.dv.comfortly.domain.repositories

import androidx.activity.result.IntentSenderRequest
import com.dv.comfortly.data.raw.interactors.TurnOnGpsInteractor
import com.dv.comfortly.data.raw.observers.GpsObserver
import com.dv.comfortly.domain.models.GpsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GpsRepository @Inject constructor(
    private val gpsObserver: GpsObserver,
    private val turnOnGpsInteractor: TurnOnGpsInteractor
) : SensorRepository.GpsRepository {

    override fun observeData(): Flow<GpsData> =
        gpsObserver.observe().map { locationData ->
            GpsData(
                latitude = locationData.latitude,
                longitude = locationData.longitude,
                altitude = locationData.altitude,
                accuracy = locationData.accuracy,
                bearing = locationData.bearing,
                bearingAccuracyDegrees = locationData.bearingAccuracyDegrees,
                speed = locationData.speed,
                speedAccuracyMetersPerSecond = locationData.speedAccuracyMetersPerSecond
            )
        }

    override suspend fun turnOnGps(): Pair<Boolean, IntentSenderRequest?> = turnOnGpsInteractor.turnOnGps()
}
