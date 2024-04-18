package com.dv.comfortly.domain.models

import com.polar.sdk.api.model.PolarEcgData
import kotlinx.datetime.Instant

data class EcgData(
    /**
     * ECG samples with:
     * - value in microVolts
     * - timestamp in nanoseconds. The epoch of timestamp is 1.1.2000
     */
    val samples: List<PolarEcgData.PolarEcgDataSample>,
    val sampleRate: Int,
) {
    companion object {
        const val POLAR_ECG_DATA_START = "2000-01-01T00:00:00.00Z"
    }
}

data class EcgDataSample(
    val id: Long = 0,
    val tripId: Long,
    val timestamp: Instant,
    val value: Int,
)
