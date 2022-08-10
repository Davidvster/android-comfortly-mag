package com.dv.comfortly.data.raw.interactors

import com.dv.comfortly.data.raw.sources.sensor.HeartRateSource
import javax.inject.Inject

interface ConnectHrDeviceInteractor {

    suspend fun connectToDevice(deviceId: String)

    suspend fun disconnectFromDevice(deviceId: String)

    class Default @Inject constructor(
        private val heartRateSource: HeartRateSource
    ) : ConnectHrDeviceInteractor {
        override suspend fun connectToDevice(deviceId: String) = heartRateSource.polarApi.connectToDevice(deviceId)

        override suspend fun disconnectFromDevice(deviceId: String) = heartRateSource.polarApi.disconnectFromDevice(deviceId)
    }
}
