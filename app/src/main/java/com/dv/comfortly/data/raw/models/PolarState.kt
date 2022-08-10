package com.dv.comfortly.data.raw.models

import com.polar.sdk.api.model.PolarDeviceInfo

data class PolarState(
    val blePowered: Boolean? = null,
    val deviceConnecting: PolarDeviceInfo? = null,
    val connectedDevice: PolarDeviceInfo? = null,
    val disconnectedDevice: PolarDeviceInfo? = null,
    val hrFeatureReady: Boolean? = null,
    val batteryLevel: Int? = null
)
