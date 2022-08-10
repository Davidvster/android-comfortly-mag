package com.dv.comfortly.data.raw.observers

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.dv.comfortly.data.errors.SensorNotFoundError
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface BaseSensorObserver : BaseObserver<SensorEvent>

private const val DEFAULT_SENSOR_SAMPLING_INTERVAL_US = 20_000

fun observeSensor(
    sensorManager: SensorManager,
    sensor: Sensor?,
    sensorName: String
): Flow<SensorEvent> = callbackFlow {
    sensor?.let {
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                this@callbackFlow.trySend(event)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        sensorManager.registerListener(sensorEventListener, it, DEFAULT_SENSOR_SAMPLING_INTERVAL_US)
        awaitClose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    } ?: throw SensorNotFoundError(sensorName)
}
