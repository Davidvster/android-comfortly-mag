package com.dv.comfortly.data.raw.models

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val accuracy: Float,
    val bearing: Float,
    val bearingAccuracyDegrees: Float,
    val speed: Float,
    val speedAccuracyMetersPerSecond: Float,
)
