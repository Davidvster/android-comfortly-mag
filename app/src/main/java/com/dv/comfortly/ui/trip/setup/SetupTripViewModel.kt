package com.dv.comfortly.ui.trip.setup

import androidx.lifecycle.SavedStateHandle
import com.dv.comfortly.domain.usecases.ConnectToHeartRateDeviceUseCase
import com.dv.comfortly.domain.usecases.DisconnectFromHeartRateDeviceUseCase
import com.dv.comfortly.domain.usecases.IsBluetoothTurnedOnUseCase
import com.dv.comfortly.domain.usecases.ObserveConnectedHeartRateDeviceUseCase
import com.dv.comfortly.domain.usecases.SearchForHeartRateDevicesUseCase
import com.dv.comfortly.domain.usecases.TurnOnLocationUseCase
import com.dv.comfortly.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class SetupTripViewModel
    @Inject
    constructor(
        private val turnOnLocationUseCase: TurnOnLocationUseCase,
        private val isBluetoothTurnedOnUseCase: IsBluetoothTurnedOnUseCase,
        private val searchForHeartRateDevicesUseCase: SearchForHeartRateDevicesUseCase,
        private val connectToHeartRateDeviceUseCase: ConnectToHeartRateDeviceUseCase,
        private val disconnectFromHeartRateDeviceUseCase: DisconnectFromHeartRateDeviceUseCase,
        private val observeConnectedHeartRateDeviceUseCase: ObserveConnectedHeartRateDeviceUseCase,
        private val savedStateHandle: SavedStateHandle,
    ) : BaseViewModel<SetupTripState, SetupTripEvent>(SetupTripState()) {
        private var tripId: Long = -1

        private var searchJob: Job? = null

        fun init(tripId: Long) {
            this.tripId = tripId
            turnOnLocation()

            launch {
                observeConnectedHeartRateDeviceUseCase()
                    .collect { heartRateDevice ->
                        hideLoading()
                        viewState =
                            viewState.copy(
                                connectedHeartRateDevice = heartRateDevice,
                                heartRateDevices = viewState.heartRateDevices - heartRateDevice,
                            )
                    }
            }
        }

        fun onBluetoothEnabledResult() {
            turnOnLocation()
        }

        fun onGpsEnabledResult(isGpsEnabled: Boolean) {
            if (isGpsEnabled) {
                onLocationStatusObtained(isGpsEnabled)
            } else {
                turnOnLocation()
            }
        }

        fun turnOnLocation() {
            launch {
                val isGpsEnabled = turnOnLocationUseCase()
                isGpsEnabled.second?.let { request ->
                    emitEvent(SetupTripEvent.TurnOnGps(request))
                }
                onLocationStatusObtained(isGpsEnabled.first)
            }
        }

        private fun onLocationStatusObtained(isGpsEnabled: Boolean) {
            launch {
                val isBluetoothEnabled = isBluetoothTurnedOnUseCase()
                viewState =
                    viewState.copy(
                        gpsTurnedOn = isGpsEnabled,
                        bluetoothTurnedOn = isBluetoothEnabled,
                        searchForDevicesEnabled = isGpsEnabled && isBluetoothEnabled,
                    )
            }
        }

        fun searchForDevices() {
            searchJob?.cancel()
            viewState =
                viewState.copy(
                    heartRateDevices = emptySet(),
                    isSearchingForHrDevices = true,
                )
            searchJob =
                launch {
                    searchForHeartRateDevicesUseCase().collect { newHeartRateDevice ->
                        viewState =
                            viewState.copy(
                                heartRateDevices = (viewState.heartRateDevices + newHeartRateDevice).toSet(),
                                isSearchingForHrDevices = false,
                            )
                    }
                }
        }

        fun onSubmitClicked() {
            emitEvent(SetupTripEvent.NavigateToCalibrateTrip(tripId))
        }

        fun connectToHrDevice(deviceId: String) {
            launchWithBlockingLoading {
                viewState.connectedHeartRateDevice?.let { disconnectFromHeartRateDeviceUseCase(it.deviceId) }
                showLoading()
                connectToHeartRateDeviceUseCase(deviceId)
            }
        }
    }
