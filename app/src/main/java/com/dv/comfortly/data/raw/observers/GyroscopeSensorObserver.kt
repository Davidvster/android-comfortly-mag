package com.dv.comfortly.data.raw.observers

import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.dv.comfortly.data.raw.sources.sensor.SensorSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GyroscopeSensorObserver : BaseSensorObserver {

    class Default @Inject constructor(
        private val sensorManager: SensorManager,
        private val sensorSource: SensorSource
    ) : GyroscopeSensorObserver {

        companion object {
            private const val GYROSCOPE_SENSOR_NAME = "Gyroscope"
        }

        override fun observe(): Flow<SensorEvent> = observeSensor(
            sensorManager = sensorManager,
            sensor = sensorSource.gyroscopeSensor?.sensor,
            sensorName = GYROSCOPE_SENSOR_NAME
        )
    }
}
