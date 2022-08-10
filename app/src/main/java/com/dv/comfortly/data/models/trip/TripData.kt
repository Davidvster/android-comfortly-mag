package com.dv.comfortly.data.models.trip

import com.dv.comfortly.data.serializers.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TripData(
    val id: Int? = null,
    val userId: String,
    val startLocationLat: Double,
    val startLocationLng: Double,
    val endLocationLat: Double,
    val endLocationLng: Double,
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant,
    val locations: List<LocationData>,
)
