package com.dv.comfortly.data.raw.sources.sensor

import android.content.Context
import com.dv.comfortly.data.raw.models.PolarState
import com.polar.sdk.api.PolarBleApi
import com.polar.sdk.api.PolarBleApiCallbackProvider
import com.polar.sdk.api.PolarBleApiDefaultImpl
import com.polar.sdk.api.model.PolarDeviceInfo
import com.polar.sdk.api.model.PolarHrData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

interface HeartRateSource {

    val polarApi: PolarBleApi

    val polarState: StateFlow<PolarState>

    class Default @Inject constructor(
        private val context: Context
    ) : HeartRateSource {

        private val state = MutableStateFlow(PolarState())

        override val polarApi: PolarBleApi by lazy {
            PolarBleApiDefaultImpl.defaultImplementation(context, PolarBleApi.ALL_FEATURES)
                .apply {
                    setApiLogger { s: String -> Timber.d("POLAR API $s") }
                }
        }

        init {
            polarApi.setApiCallback(object : PolarBleApiCallbackProvider {
                override fun blePowerStateChanged(powered: Boolean) {
                    state.value = state.value.copy(
                        blePowered = powered
                    )
                }

                override fun deviceConnected(polarDeviceInfo: PolarDeviceInfo) {
                    state.value = state.value.copy(
                        connectedDevice = polarDeviceInfo
                    )
                }

                override fun deviceConnecting(polarDeviceInfo: PolarDeviceInfo) {
                    state.value = state.value.copy(
                        deviceConnecting = polarDeviceInfo
                    )
                }

                override fun deviceDisconnected(polarDeviceInfo: PolarDeviceInfo) {
                    state.value = state.value.copy(
                        connectedDevice = if (polarDeviceInfo == state.value.connectedDevice) null else state.value.connectedDevice,
                        disconnectedDevice = polarDeviceInfo,
                        hrFeatureReady = false
                    )
                }

                override fun streamingFeaturesReady(identifier: String, features: MutableSet<PolarBleApi.DeviceStreamingFeature>) = Unit

                override fun sdkModeFeatureAvailable(identifier: String) = Unit

                override fun hrFeatureReady(identifier: String) {
                    if (identifier == state.value.connectedDevice?.deviceId) {
                        state.value = state.value.copy(
                            hrFeatureReady = true
                        )
                    }
                }

                override fun disInformationReceived(identifier: String, uuid: UUID, value: String) = Unit

                override fun batteryLevelReceived(identifier: String, level: Int) {
                    if (identifier == state.value.connectedDevice?.deviceId) {
                        state.value = state.value.copy(
                            batteryLevel = level
                        )
                    }
                }

                override fun hrNotificationReceived(identifier: String, data: PolarHrData) = Unit

                override fun polarFtpFeatureReady(identifier: String) = Unit
            })
        }

        override val polarState: StateFlow<PolarState> = state
    }
}
