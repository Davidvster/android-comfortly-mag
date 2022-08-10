package com.dv.comfortly.domain.models

data class HeartRateData(
    /**
     * Pulse to Pulse interval in milliseconds.
     */
//    val ppi: Int,
    /**
     * Error estimate of the expected absolute error in PP-interval in milliseconds. The value indicates the quality of PP-intervals.
     * When error estimate is below 10ms the PP-intervals are probably very accurate. Error estimate values over 30ms may be caused
     * by movement artefact or too loose sensor-skin contact.
     */
//    val errorEstimate: Int,
    /**
     * Heart rate in beats per minute.
     */
    val heartRate: Int,
    /**
     * True if PPI measurement is invalid due to acceleration (or other reason).
     */
//    val blockerBit: Boolean,
    /**
     * False if the device detects poor or no contact with the skin.
     */
//    val skinContactStatus: Boolean,
    /**
     * True if the Sensor Contact feature is supported.
     */
//    val skinContactSupported: Boolean
)
