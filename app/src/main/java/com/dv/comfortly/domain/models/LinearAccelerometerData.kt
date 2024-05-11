package com.dv.comfortly.domain.models

data class LinearAccelerometerData(
    val xAxisLinearAcceleration: Float,
    val yAxisLinearAcceleration: Float,
    val zAxisLinearAcceleration: Float,
    val accuracy: Int?,
)
