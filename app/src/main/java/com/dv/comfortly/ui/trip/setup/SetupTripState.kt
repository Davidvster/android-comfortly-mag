package com.dv.comfortly.ui.trip.setup

import com.dv.comfortly.domain.models.HeartRateDevice

data class SetupTripState(
    val gpsTurnedOn: Boolean = false,
    val bluetoothTurnedOn: Boolean = false,
    val searchForDevicesEnabled: Boolean = false,
    val isSearchingForHrDevices: Boolean = false,
    val connectedHeartRateDevice: HeartRateDevice? = null,
    val heartRateDevices: Set<HeartRateDevice> = emptySet(),
    val submitEnabled: Boolean = false
)
