package com.dv.comfortly.data.raw.observers

import android.hardware.SensorManager
import com.dv.comfortly.data.raw.models.SensorEventWithAccuracy
import com.dv.comfortly.data.raw.sources.sensor.SensorSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RotationVectorSensorObserver : BaseSensorObserver {
    class Default
        @Inject
        constructor(
            private val sensorManager: SensorManager,
            private val sensorSource: SensorSource,
        ) : RotationVectorSensorObserver {
            companion object {
                private const val ROTATION_VECTOR_SENSOR_NAME = "RotationVector"
            }

            override fun observe(): Flow<SensorEventWithAccuracy> =
                observeSensor(
                    sensorManager = sensorManager,
                    observedSensor = sensorSource.rotationVectorSensor?.sensor,
                    sensorName = ROTATION_VECTOR_SENSOR_NAME,
                )
        }
}
