package com.dv.comfortly.data.mappers

import com.dv.comfortly.domain.models.AccelerometerData
import com.dv.comfortly.domain.models.GpsData
import com.dv.comfortly.domain.models.GravityData
import com.dv.comfortly.domain.models.GyroscopeData
import com.dv.comfortly.domain.models.HeartRateData
import com.dv.comfortly.domain.models.LinearAccelerometerData
import com.dv.comfortly.domain.models.RotationVectorData
import com.dv.comfortly.domain.models.SensorData
import com.dv.comfortly.domain.models.TripDatapoint

private typealias DbTripCalibrationDatapoint = com.dv.comfortly.data.raw.db.entity.TripCalibrationDatapoint

object TripCalibrationDatapointMapper {
    fun dbToDomain(data: DbTripCalibrationDatapoint): TripDatapoint =
        TripDatapoint(
            id = data.id,
            tripId = data.tripId,
            timestamp = data.timestamp,
            sensorData =
                SensorData(
                    accelerometerData =
                        AccelerometerData(
                            xAxisAcceleration = data.accelerometerData.xAxis,
                            yAxisAcceleration = data.accelerometerData.yAxis,
                            zAxisAcceleration = data.accelerometerData.zAxis,
                        ),
                    gravityData =
                        GravityData(
                            xAxisGravity = data.gravityData.xAxis,
                            yAxisGravity = data.gravityData.yAxis,
                            zAxisGravity = data.gravityData.zAxis,
                        ),
                    gyroscopeData =
                        GyroscopeData(
                            xAxisRotationRate = data.gyroscopeData.xAxis,
                            yAxisRotationRate = data.gyroscopeData.yAxis,
                            zAxisRotationRate = data.gyroscopeData.zAxis,
                        ),
                    linearAccelerometerData =
                        LinearAccelerometerData(
                            xAxisLinearAcceleration = data.linearAccelerometerData.xAxis,
                            yAxisLinearAcceleration = data.linearAccelerometerData.yAxis,
                            zAxisLinearAcceleration = data.linearAccelerometerData.zAxis,
                        ),
                    rotationVectorData =
                        RotationVectorData(
                            xAxisRotationVector = data.rotationVectorData.xAxis,
                            yAxisRotationVector = data.rotationVectorData.yAxis,
                            zAxisRotationVector = data.rotationVectorData.zAxis,
                            rotationVectorScalar = data.rotationVectorData.scalar,
                        ),
                ),
            gpsData =
                GpsData(
                    latitude = data.gpsData.latitude,
                    longitude = data.gpsData.longitude,
                    altitude = data.gpsData.altitude,
                    accuracy = data.gpsData.accuracy,
                    bearing = data.gpsData.bearing,
                    bearingAccuracyDegrees = data.gpsData.bearingAccuracyDegrees,
                    speed = data.gpsData.speed,
                    speedAccuracyMetersPerSecond = data.gpsData.speedAccuracyMetersPerSecond,
                ),
            heartRateData =
                HeartRateData(
                    heartRate = data.heartRateData.heartRate,
                ),
        )

    fun domainToDb(data: TripDatapoint): DbTripCalibrationDatapoint =
        DbTripCalibrationDatapoint(
            tripId = data.tripId,
            timestamp = data.timestamp,
            accelerometerData =
                DbSensorData(
                    xAxis = data.sensorData.accelerometerData.xAxisAcceleration,
                    yAxis = data.sensorData.accelerometerData.yAxisAcceleration,
                    zAxis = data.sensorData.accelerometerData.zAxisAcceleration,
                ),
            gravityData =
                DbSensorData(
                    xAxis = data.sensorData.gravityData.xAxisGravity,
                    yAxis = data.sensorData.gravityData.yAxisGravity,
                    zAxis = data.sensorData.gravityData.zAxisGravity,
                ),
            gyroscopeData =
                DbSensorData(
                    xAxis = data.sensorData.gyroscopeData.xAxisRotationRate,
                    yAxis = data.sensorData.gyroscopeData.yAxisRotationRate,
                    zAxis = data.sensorData.gyroscopeData.zAxisRotationRate,
                ),
            linearAccelerometerData =
                DbSensorData(
                    xAxis = data.sensorData.linearAccelerometerData.xAxisLinearAcceleration,
                    yAxis = data.sensorData.linearAccelerometerData.yAxisLinearAcceleration,
                    zAxis = data.sensorData.linearAccelerometerData.zAxisLinearAcceleration,
                ),
            rotationVectorData =
                DbRotationVectorSensorData(
                    xAxis = data.sensorData.rotationVectorData.xAxisRotationVector,
                    yAxis = data.sensorData.rotationVectorData.yAxisRotationVector,
                    zAxis = data.sensorData.rotationVectorData.zAxisRotationVector,
                    scalar = data.sensorData.rotationVectorData.rotationVectorScalar,
                ),
            gpsData =
                DbGpsData(
                    latitude = data.gpsData.latitude,
                    longitude = data.gpsData.longitude,
                    altitude = data.gpsData.altitude,
                    accuracy = data.gpsData.accuracy,
                    bearing = data.gpsData.bearing,
                    bearingAccuracyDegrees = data.gpsData.bearingAccuracyDegrees,
                    speed = data.gpsData.speed,
                    speedAccuracyMetersPerSecond = data.gpsData.speedAccuracyMetersPerSecond,
                ),
            heartRateData =
                DbHeartRateData(
                    heartRate = data.heartRateData.heartRate,
                ),
        )
}
