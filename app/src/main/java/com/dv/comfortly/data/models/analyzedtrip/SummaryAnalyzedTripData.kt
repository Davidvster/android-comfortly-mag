package com.dv.comfortly.data.models.analyzedtrip

import com.dv.comfortly.data.serializers.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class SummaryAnalyzedTripData(
    val id: Int,
    val tripId: Int,
    val userId: String,
    val startLocationName: String,
    val endLocationName: String,
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant,
    val distance: Double,
    val comfortLevel: Double,
)
