package com.dv.comfortly.domain.repositories

import com.dv.comfortly.data.raw.interactors.ConnectHrDeviceInteractor
import com.dv.comfortly.data.raw.interactors.ObserveEcgDataInteractor
import com.dv.comfortly.data.raw.interactors.ObserveHrDataInteractor
import com.dv.comfortly.data.raw.interactors.ObserveHrDeviceConnectedInteractor
import com.dv.comfortly.data.raw.interactors.SearchHrDevicesInteractor
import com.dv.comfortly.domain.models.EcgData
import com.dv.comfortly.domain.models.HeartRateData
import com.dv.comfortly.domain.models.HeartRateDevice
import com.polar.sdk.api.model.PolarDeviceInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onErrorResume
import timber.log.Timber
import javax.inject.Inject

interface HeartRateRepository {
    fun observeHeartRate(): Flow<HeartRateData>

    fun observeEcg(): Flow<EcgData>

    fun observeDeviceConnected(): Flow<HeartRateDevice>

    fun searchDevices(): Flow<HeartRateDevice>

    suspend fun connectDevice(deviceId: String)

    suspend fun disconnectDevice(deviceId: String)

    class Default
        @Inject
        constructor(
            private val observeHrDataInteractor: ObserveHrDataInteractor,
            private val searchHrDevicesInteractor: SearchHrDevicesInteractor,
            private val connectHrDeviceInteractor: ConnectHrDeviceInteractor,
            private val observeHrDeviceConnectedInteractor: ObserveHrDeviceConnectedInteractor,
            private val observeEcgDataInteractor: ObserveEcgDataInteractor,
        ) : HeartRateRepository {
            override fun observeHeartRate(): Flow<HeartRateData> =
                observeHrDataInteractor.observeHeartRateData()
                    //            .onEach { Timber.d("COMFORTLY HEARTRATE ${it.hr} at time ${Clock.System.now()}") }
                    .map { data -> HeartRateData(data.hr) }

            override fun observeEcg(): Flow<EcgData> =
                observeEcgDataInteractor.observeEcgData()
                    //            .onEach {
                    //                val timestamp = Instant.parse(POLAR_ECG_DATA_START) + it.first.timeStamp.nanoseconds
                    //                val current = Clock.System.now()
                    //                val diff = current - timestamp
                    //                Timber.d("COMFORTLY ECG: Got timestamp: $timestamp, current time: $current, diff: $diff")
                    //            }
                    .catch {
                        Timber.e(it)
                        emitAll(observeEcgDataInteractor.observeEcgData())
                    }
                    .map { (ecg, sampleRate) ->
                        EcgData(
                            samples = ecg.samples,
                            sampleRate = sampleRate,
                        )
                    }

            override fun observeDeviceConnected(): Flow<HeartRateDevice> =
                observeHrDeviceConnectedInteractor.observeConnectedDevices().map { polarDevice ->
                    polarDevice.toHeartRateDevice()
                }

            override fun searchDevices(): Flow<HeartRateDevice> =
                searchHrDevicesInteractor.searchForDevices().map { polarDevice ->
                    polarDevice.toHeartRateDevice()
                }

            override suspend fun connectDevice(deviceId: String) = connectHrDeviceInteractor.connectToDevice(deviceId)

            override suspend fun disconnectDevice(deviceId: String) = connectHrDeviceInteractor.disconnectFromDevice(deviceId)

            private fun PolarDeviceInfo.toHeartRateDevice() =
                HeartRateDevice(
                    deviceId = deviceId,
                    address = address,
                    name = name,
                )
        }
}
