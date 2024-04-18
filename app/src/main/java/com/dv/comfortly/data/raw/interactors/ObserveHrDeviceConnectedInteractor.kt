package com.dv.comfortly.data.raw.interactors

import com.dv.comfortly.data.raw.sources.sensor.HeartRateSource
import com.polar.sdk.api.model.PolarDeviceInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ObserveHrDeviceConnectedInteractor {
    fun observeConnectedDevices(): Flow<PolarDeviceInfo>

    class Default
        @Inject
        constructor(
            private val heartRateSource: HeartRateSource,
        ) : ObserveHrDeviceConnectedInteractor {
            override fun observeConnectedDevices(): Flow<PolarDeviceInfo> =
                heartRateSource.polarState
                    .map { it.connectedDevice }
                    .filterNotNull()
                    .distinctUntilChanged()
        }
}
