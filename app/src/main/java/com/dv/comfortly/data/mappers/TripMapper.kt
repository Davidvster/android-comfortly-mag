package com.dv.comfortly.data.mappers

import com.dv.comfortly.domain.models.Trip

private typealias TripWithData = com.dv.comfortly.data.raw.db.entity.TripWithData

object TripMapper {
    fun dbToDomain(data: TripWithData): Trip =
        Trip(
            id = data.id,
            name = data.name,
            data = data.dataPoints.map { TripDatapointMapper.dbToDomain(it) },
            ecgData = data.ecgDataPoints.map { TripEcgSampleMapper.dbToDomain(it) },
            calibrationData = data.calibrationDataPoints.map { TripCalibrationDatapointMapper.dbToDomain(it) },
            ecgCalibrationData = data.ecgCalibrationDataPoints.map { TripEcgCalibrationSampleMapper.dbToDomain(it) },
        )
}
