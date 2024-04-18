package com.dv.comfortly.data.raw.observers

import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.dv.comfortly.data.raw.sources.sensor.SensorSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GravitySensorObserver : BaseSensorObserver {
    class Default
        @Inject
        constructor(
            private val sensorManager: SensorManager,
            private val sensorSource: SensorSource,
        ) : GravitySensorObserver {
            companion object {
                private const val GRAVITY_SENSOR_NAME = "Gravity"
            }

            override fun observe(): Flow<SensorEvent> =
                observeSensor(
                    sensorManager = sensorManager,
                    sensor = sensorSource.gravitySensor?.sensor,
                    sensorName = GRAVITY_SENSOR_NAME,
                )
        }
}
