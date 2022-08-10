package com.dv.comfortly.ui.trip.recordtrip

sealed class RecordTripEvent {
    data class NavigateToCalibrateTrip(val tripId: Long) : RecordTripEvent()
    data class NavigateToQuestionnaire(val tripId: Long) : RecordTripEvent()
    data class NavigateToRecordTrip(val tripId: Long) : RecordTripEvent()
}
