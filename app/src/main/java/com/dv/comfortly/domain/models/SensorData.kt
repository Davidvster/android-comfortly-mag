package com.dv.comfortly.domain.models

data class SensorData(
    val accelerometerData: AccelerometerData,
    val gravityData: GravityData,
    val gyroscopeData: GyroscopeData,
    val linearAccelerometerData: LinearAccelerometerData,
    val rotationVectorData: RotationVectorData,
)
