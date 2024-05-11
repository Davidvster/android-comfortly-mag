package com.dv.comfortly.data.raw.observers

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.dv.comfortly.data.errors.SensorNotFoundError
import com.dv.comfortly.data.raw.models.SensorEventWithAccuracy
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface BaseSensorObserver : BaseObserver<SensorEventWithAccuracy>

private const val DEFAULT_SENSOR_SAMPLING_INTERVAL_US = 20_000

fun observeSensor(
    sensorManager: SensorManager,
    observedSensor: Sensor?,
    sensorName: String,
): Flow<SensorEventWithAccuracy> =
    callbackFlow {
        var observedAccuracy: Int? = null
        observedSensor?.let {
            val sensorEventListener =
                object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent) {
                        this@callbackFlow.trySend(SensorEventWithAccuracy(event, observedAccuracy))
                    }

                    override fun onAccuracyChanged(
                        sensor: Sensor?,
                        accuracy: Int,
                    ) {
                        if (sensor?.name == observedSensor.name)
                            {
                                observedAccuracy = accuracy
                            }
                    }
                }

            sensorManager.registerListener(sensorEventListener, it, DEFAULT_SENSOR_SAMPLING_INTERVAL_US)
            awaitClose {
                sensorManager.unregisterListener(sensorEventListener)
            }
        } ?: throw SensorNotFoundError(sensorName)
    }
