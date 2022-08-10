package com.dv.comfortly.data.raw.observers

import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.dv.comfortly.data.raw.sources.sensor.SensorSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LinearAccelerationSensorObserver : BaseSensorObserver {

    class Default @Inject constructor(
        private val sensorManager: SensorManager,
        private val sensorSource: SensorSource
    ) : LinearAccelerationSensorObserver {

        companion object {
            private const val LINEAR_ACCELERATION_SENSOR_NAME = "LinearAcceleration"
        }

        override fun observe(): Flow<SensorEvent> = observeSensor(
            sensorManager = sensorManager,
            sensor = sensorSource.linearAccelerationSensor?.sensor,
            sensorName = LINEAR_ACCELERATION_SENSOR_NAME
        )
    }
}
