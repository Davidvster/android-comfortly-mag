package com.dv.comfortly.ui.trip.details

sealed class TripDetailsEvent {
    class AskForDocument(val name: String) : TripDetailsEvent()
    class OpenDocument(val uri: String) : TripDetailsEvent()
}
