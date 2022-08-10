package com.dv.comfortly.ui.trip.recordtrip

import com.dv.comfortly.domain.models.GpsData
import com.github.mikephil.charting.data.Entry
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class NewTripState(
    val accelerometer: GraphData? = null,
    val gravity: GraphData? = null,
    val gyroscope: GraphData? = null,
    val linearAcceleration: GraphData? = null,
    val rotationVector: RotationVectorGraphData? = null,
    val locations: List<GpsData> = emptyList(),
    val heartRate: HeartRateGraphData? = null,
    val ecgData: EcgGraphData? = null,
    val isForCalibration: Boolean = false,
    val calibrationTime: Duration = 0.seconds
)

data class GraphData(
    val xAxis: List<Entry>,
    val yAxis: List<Entry>,
    val zAxis: List<Entry>
)

data class RotationVectorGraphData(
    val xAxis: List<Entry>,
    val yAxis: List<Entry>,
    val zAxis: List<Entry>,
    val scalar: List<Entry>
)

data class HeartRateGraphData(
    val heartRate: List<Entry>
)

data class EcgGraphData(
    val ecg: List<Entry>
)

enum class RecordTripType {
    TEST,
    CALIBRATE,
    RECORD
}
