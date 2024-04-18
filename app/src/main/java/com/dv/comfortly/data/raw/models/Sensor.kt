package com.dv.comfortly.data.raw.models

import android.hardware.Sensor

@JvmInline
value class AccelerometerSensor(
    val sensor: Sensor,
)

@JvmInline
value class GravitySensor(
    val sensor: Sensor,
)

@JvmInline
value class GyroscopeSensor(
    val sensor: Sensor,
)

@JvmInline
value class LinearAccelerationSensor(
    val sensor: Sensor,
)

@JvmInline
value class RotationVectorSensor(
    val sensor: Sensor,
)
