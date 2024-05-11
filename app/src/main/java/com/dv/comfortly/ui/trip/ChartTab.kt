package com.dv.comfortly.ui.trip

import androidx.annotation.StringRes
import com.dv.comfortly.R

enum class ChartTab(
    @StringRes val stringRes: Int,
) {
    HEART_RATE(R.string.heart_rate),
    ECG(R.string.ecg),
    ACCELEROMETER(R.string.accelerometer),
    GRAVITY(R.string.gravity),
    GYROSCOPE(R.string.gyroscope),
    GYROSCOPE_ORIENTATION(R.string.gyroscope_orientation),
    LINEAR_ACCELERATION(R.string.linear_acceleration),
    ROTATION_VECTOR(R.string.rotation_vector),
    ROTATION_VECTOR_ORIENTATION(R.string.rotation_vector_orientation),
}
