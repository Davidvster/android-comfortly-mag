package com.dv.comfortly.data.mappers

import com.dv.comfortly.domain.models.EcgDataSample

private typealias DbTripEcgCalibrationSample = com.dv.comfortly.data.raw.db.entity.TripEcgCalibrationSample

object TripEcgCalibrationSampleMapper {
    fun domainToDb(data: EcgDataSample): DbTripEcgCalibrationSample =
        DbTripEcgCalibrationSample(
            id = data.id,
            tripId = data.tripId,
            timestamp = data.timestamp,
            ecgValue = data.value,
        )

    fun dbToDomain(data: DbTripEcgCalibrationSample): EcgDataSample =
        EcgDataSample(
            id = data.id,
            tripId = data.tripId,
            timestamp = data.timestamp,
            value = data.ecgValue,
        )
}
