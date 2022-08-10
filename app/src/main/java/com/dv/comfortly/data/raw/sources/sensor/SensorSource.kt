package com.dv.comfortly.data.raw.sources.sensor

import android.hardware.Sensor
import android.hardware.SensorManager
import com.dv.comfortly.data.raw.models.AccelerometerSensor
import com.dv.comfortly.data.raw.models.GravitySensor
import com.dv.comfortly.data.raw.models.GyroscopeSensor
import com.dv.comfortly.data.raw.models.LinearAccelerationSensor
import com.dv.comfortly.data.raw.models.RotationVectorSensor
import javax.inject.Inject

interface SensorSource {

    val accelerometerSensor: AccelerometerSensor?

    val gravitySensor: GravitySensor?

    val gyroscopeSensor: GyroscopeSensor?

    val linearAccelerationSensor: LinearAccelerationSensor?

    val rotationVectorSensor: RotationVectorSensor?

    class Default @Inject constructor(
        private val sensorManager: SensorManager
    ) : SensorSource {

        override val accelerometerSensor: AccelerometerSensor?
            get() = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let { AccelerometerSensor(it) }

        override val gravitySensor: GravitySensor?
            get() = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)?.let { GravitySensor(it) }

        override val gyroscopeSensor: GyroscopeSensor?
            get() = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let { GyroscopeSensor(it) }

        override val linearAccelerationSensor: LinearAccelerationSensor?
            get() = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)?.let { LinearAccelerationSensor(it) }

        override val rotationVectorSensor: RotationVectorSensor?
            get() = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.let { RotationVectorSensor(it) }
    }
}
