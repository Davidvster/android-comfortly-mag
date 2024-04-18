package com.dv.comfortly.data.raw.interactors

import com.dv.comfortly.data.raw.sources.sensor.HeartRateSource
import com.polar.sdk.api.model.PolarDeviceInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

interface SearchHrDevicesInteractor {
    fun searchForDevices(): Flow<PolarDeviceInfo>

    class Default
        @Inject
        constructor(
            private val heartRateSource: HeartRateSource,
        ) : SearchHrDevicesInteractor {
            override fun searchForDevices(): Flow<PolarDeviceInfo> =
                heartRateSource.polarApi.searchForDevice().asFlow().filter { it.isConnectable }
        }
}
