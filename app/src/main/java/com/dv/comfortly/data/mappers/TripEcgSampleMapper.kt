package com.dv.comfortly.data.mappers

import com.dv.comfortly.domain.models.EcgDataSample

private typealias DbTripEcgSample = com.dv.comfortly.data.raw.db.entity.TripEcgSample

object TripEcgSampleMapper {
    fun domainToDb(data: EcgDataSample): DbTripEcgSample =
        DbTripEcgSample(
            id = data.id,
            tripId = data.tripId,
            timestamp = data.timestamp,
            ecgValue = data.value,
        )

    fun dbToDomain(data: DbTripEcgSample): EcgDataSample =
        EcgDataSample(
            id = data.id,
            tripId = data.tripId,
            timestamp = data.timestamp,
            value = data.ecgValue,
        )
}
