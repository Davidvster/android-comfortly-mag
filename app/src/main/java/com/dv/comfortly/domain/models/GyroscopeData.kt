package com.dv.comfortly.domain.models

data class GyroscopeData(
    val xAxisRotationRate: Float,
    val yAxisRotationRate: Float,
    val zAxisRotationRate: Float,
    val orientationX: Float,
    val orientationY: Float,
    val orientationZ: Float,
)
