package com.dv.comfortly.domain.usecases.params

import java.io.OutputStream

data class ExportTripParams(
    val tripId: Long,
    val outputStream: OutputStream
)
