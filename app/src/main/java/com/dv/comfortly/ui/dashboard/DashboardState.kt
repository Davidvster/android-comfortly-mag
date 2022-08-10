package com.dv.comfortly.ui.dashboard

import com.dv.comfortly.domain.models.TripSummary

sealed class DashboardState {
    object Idle : DashboardState()
    data class Trips(val trips: List<TripSummary>) : DashboardState()
}
