package com.dv.comfortly.data.raw.models

import android.hardware.SensorEvent

data class SensorEventWithAccuracy(
    val event: SensorEvent,
    val accuracy: Int?,
)
