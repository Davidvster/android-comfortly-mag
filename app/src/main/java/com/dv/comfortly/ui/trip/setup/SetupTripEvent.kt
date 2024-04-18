package com.dv.comfortly.ui.trip.setup

import androidx.activity.result.IntentSenderRequest

sealed class SetupTripEvent {
    object NoHrDevicesFound : SetupTripEvent()

    data class TurnOnGps(val request: IntentSenderRequest) : SetupTripEvent()

    data class NavigateToCalibrateTrip(val tripId: Long) : SetupTripEvent()
}
