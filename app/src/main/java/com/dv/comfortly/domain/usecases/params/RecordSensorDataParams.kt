package com.dv.comfortly.domain.usecases.params

import com.dv.comfortly.ui.trip.recordtrip.RecordTripType

data class RecordSensorDataParams(
    val tripId: Long,
    val recordTripType: RecordTripType
)
