package com.dv.comfortly.data.models.trip

import com.dv.comfortly.data.serializers.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class LocationData(
    val id: Int? = null,
    val locationLat: Double,
    val locationLng: Double,
    val speed: Double,
    val acceleration: Double,
    val orientation: Double,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant,
)
