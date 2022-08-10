package com.dv.comfortly.domain.models

import kotlinx.datetime.Instant

data class Trip(
    val id: Long = 0,
    val name: String,
    val data: List<TripDatapoint> = emptyList(),
    val ecgData: List<EcgDataSample> = emptyList(),
    val calibrationData: List<TripDatapoint> = emptyList(),
    val ecgCalibrationData: List<EcgDataSample> = emptyList()
)

data class TripSummary(
    val id: Long,
    val name: String,
    val startTime: Instant?,
    val endTime: Instant?
)

data class TripDatapoint(
    val id: Long = 0,
    val tripId: Long,
    val timestamp: Instant,
    val sensorData: SensorData,
    val gpsData: GpsData,
    val heartRateData: HeartRateData
)
