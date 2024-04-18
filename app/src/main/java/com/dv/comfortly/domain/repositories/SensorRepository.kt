package com.dv.comfortly.domain.repositories

import androidx.activity.result.IntentSenderRequest
import com.dv.comfortly.domain.models.AccelerometerData
import com.dv.comfortly.domain.models.GpsData
import com.dv.comfortly.domain.models.GravityData
import com.dv.comfortly.domain.models.GyroscopeData
import com.dv.comfortly.domain.models.LinearAccelerometerData
import com.dv.comfortly.domain.models.RotationVectorData
import kotlinx.coroutines.flow.Flow

interface SensorRepository<T> {
    fun observeData(): Flow<T>

    interface Accelerometer : SensorRepository<AccelerometerData>

    interface Gravity : SensorRepository<GravityData>

    interface Gyroscope : SensorRepository<GyroscopeData>

    interface LinearAcceleration : SensorRepository<LinearAccelerometerData>

    interface RotationVector : SensorRepository<RotationVectorData>

    interface GpsRepository : SensorRepository<GpsData> {
        suspend fun turnOnGps(): Pair<Boolean, IntentSenderRequest?>
    }
}
