package com.dv.comfortly.data.raw.interactors

import com.dv.comfortly.data.raw.sources.sensor.HeartRateSource
import com.polar.sdk.api.PolarBleApi
import com.polar.sdk.api.model.PolarEcgData
import com.polar.sdk.api.model.PolarSensorSetting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.rx3.await
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

interface ObserveEcgDataInteractor {
    fun observeEcgData(): Flow<Pair<PolarEcgData, Int>>

    class Default
        @Inject
        constructor(
            private val heartRateSource: HeartRateSource,
        ) : ObserveEcgDataInteractor {
            companion object {
                private const val DEFAULT_ECG_SAMPLE_RATE_HZ = 130
                private const val UTC_TIMEZONE = "UTC"
            }

            @OptIn(ExperimentalCoroutinesApi::class)
            override fun observeEcgData(): Flow<Pair<PolarEcgData, Int>> =
                heartRateSource.polarState.value.connectedDevice?.let { hrDevice ->
                    flow {
                        val availableSettingsEcg =
                            runCatching {
                                heartRateSource.polarApi.requestStreamSettings(
                                    identifier = hrDevice.deviceId,
                                    feature = PolarBleApi.PolarDeviceDataType.ECG
                                ).await()
                            }.getOrNull()
                        val sampleRate =
                            availableSettingsEcg?.settings?.get(PolarSensorSetting.SettingType.SAMPLE_RATE)
                                ?.firstOrNull() ?: DEFAULT_ECG_SAMPLE_RATE_HZ
                        requireNotNull(availableSettingsEcg)
                            heartRateSource.polarApi.setLocalTime(hrDevice.deviceId, Calendar.getInstance(TimeZone.getTimeZone(UTC_TIMEZONE)))
                                .await()
                        emit(availableSettingsEcg to sampleRate)
                    }.flatMapConcat { (settings, sampleRate) ->
                        heartRateSource.polarApi.startEcgStreaming(hrDevice.deviceId, settings).asFlow()
                            .map { it to sampleRate }
                    }
                } ?: emptyFlow()
        }
}
