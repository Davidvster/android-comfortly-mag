package com.dv.comfortly.data.raw.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dv.comfortly.data.raw.db.adapters.InstantTypeAdapter
import kotlinx.datetime.Instant

@Entity(
    tableName = "trip_datapoint",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
@TypeConverters(InstantTypeAdapter::class)
data class TripDatapoint(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "trip_id") val tripId: Long,
    @ColumnInfo(name = "timestamp") val timestamp: Instant,
    @Embedded(prefix = "accelerometer_") val accelerometerData: SensorData,
    @Embedded(prefix = "gravity_") val gravityData: SensorData,
    @Embedded(prefix = "gyroscope_") val gyroscopeData: SensorData,
    @Embedded(prefix = "linear_accelerometer_") val linearAccelerometerData: SensorData,
    @Embedded(prefix = "rotation_vector_") val rotationVectorData: RotationVectorSensorData,
    @Embedded(prefix = "gps_") val gpsData: GpsData,
    @Embedded(prefix = "hr_") val heartRateData: HeartRateData,
)

data class SensorData(
    @ColumnInfo(name = "x_axis") val xAxis: Float,
    @ColumnInfo(name = "y_axis") val yAxis: Float,
    @ColumnInfo(name = "z_axis") val zAxis: Float,
)

data class RotationVectorSensorData(
    @ColumnInfo(name = "x_axis") val xAxis: Float,
    @ColumnInfo(name = "y_axis") val yAxis: Float,
    @ColumnInfo(name = "z_axis") val zAxis: Float,
    @ColumnInfo(name = "scalar") val scalar: Float,
)

data class GpsData(
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "altitude") val altitude: Double,
    @ColumnInfo(name = "accuracy") val accuracy: Float,
    @ColumnInfo(name = "bearing") val bearing: Float,
    @ColumnInfo(name = "bearingAccuracyDegrees") val bearingAccuracyDegrees: Float,
    @ColumnInfo(name = "speed") val speed: Float,
    @ColumnInfo(name = "speedAccuracyMetersPerSecond") val speedAccuracyMetersPerSecond: Float,
)

data class HeartRateData(
    @ColumnInfo(name = "heart_rate") val heartRate: Int,
)

@Entity(
    tableName = "trip_ecg_sample",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
@TypeConverters(InstantTypeAdapter::class)
data class TripEcgSample(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "trip_id") val tripId: Long,
    @ColumnInfo(name = "timestamp") val timestamp: Instant,
    @ColumnInfo(name = "ecg_value_mv") val ecgValue: Int,
)
