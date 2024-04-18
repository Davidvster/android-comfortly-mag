package com.dv.comfortly.ui.dashboard

import com.dv.comfortly.domain.models.TripSummary

data class DashboardState(
    val trips: List<TripSummary> = emptyList(),
    val hasNewTrip: Boolean = false,
)
