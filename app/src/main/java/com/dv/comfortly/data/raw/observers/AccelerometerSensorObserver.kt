package com.dv.comfortly.data.raw.observers

import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.dv.comfortly.data.raw.sources.sensor.SensorSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AccelerometerSensorObserver : BaseSensorObserver {
    class Default
        @Inject
        constructor(
            private val sensorManager: SensorManager,
            private val sensorSource: SensorSource,
        ) : AccelerometerSensorObserver {
            companion object {
                private const val ACCELEROMETER_SENSOR_NAME = "Accelerometer"
            }

            override fun observe(): Flow<SensorEvent> =
                observeSensor(
                    sensorManager = sensorManager,
                    sensor = sensorSource.accelerometerSensor?.sensor,
                    sensorName = ACCELEROMETER_SENSOR_NAME,
                )
        }
}
