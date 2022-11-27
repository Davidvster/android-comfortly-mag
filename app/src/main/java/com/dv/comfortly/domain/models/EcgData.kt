package com.dv.comfortly.domain.models

import kotlinx.datetime.Instant

data class EcgData(
    /**
     * Last sample timestamp in nanoseconds. The epoch of timestamp is 1.1.2000
     */
    val lastSampleTimestamp: Instant,
    /**
     * ECG samples in microVolts.
     */
    val samples: List<Int>,
    val sampleRate: Int
) {

    companion object {
        const val POLAR_ECG_DATA_START = "2000-01-01T00:00:00.00Z"
    }
}

data class EcgDataSample(
    val id: Long = 0,
    val tripId: Long,
    val timestamp: Instant,
    val value: Int
)
