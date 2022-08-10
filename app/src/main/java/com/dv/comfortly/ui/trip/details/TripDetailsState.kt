package com.dv.comfortly.ui.trip.details

import com.dv.comfortly.domain.models.GpsData
import com.dv.comfortly.ui.trip.recordtrip.EcgGraphData
import com.dv.comfortly.ui.trip.recordtrip.GraphData
import com.dv.comfortly.ui.trip.recordtrip.HeartRateGraphData
import com.dv.comfortly.ui.trip.recordtrip.RotationVectorGraphData
import kotlinx.datetime.Instant

data class TripDetailsState(
    val tripId: Long? = null,
    val tripName: String? = null,
    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val accelerometer: GraphData? = null,
    val gravity: GraphData? = null,
    val gyroscope: GraphData? = null,
    val linearAcceleration: GraphData? = null,
    val rotationVector: RotationVectorGraphData? = null,
    val locations: List<GpsData> = emptyList(),
    val heartRate: HeartRateGraphData? = null,
    val ecg: EcgGraphData? = null
)
