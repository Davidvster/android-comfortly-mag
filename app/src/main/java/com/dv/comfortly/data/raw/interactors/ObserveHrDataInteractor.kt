package com.dv.comfortly.data.raw.interactors

import com.dv.comfortly.data.raw.sources.sensor.HeartRateSource
import com.polar.sdk.api.model.PolarHrBroadcastData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

interface ObserveHrDataInteractor {
    fun observeHeartRateData(): Flow<PolarHrBroadcastData>

    class Default
        @Inject
        constructor(
            private val heartRateSource: HeartRateSource,
        ) : ObserveHrDataInteractor {
            override fun observeHeartRateData(): Flow<PolarHrBroadcastData> =
                heartRateSource.polarState.value.connectedDevice?.let { hrDevice ->
                    heartRateSource.polarApi.startListenForPolarHrBroadcasts(setOf(hrDevice.deviceId)).asFlow()
                } ?: emptyFlow()
        }
}
