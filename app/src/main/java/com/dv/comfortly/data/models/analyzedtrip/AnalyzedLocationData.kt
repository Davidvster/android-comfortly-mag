package com.dv.comfortly.data.models.analyzedtrip

import com.dv.comfortly.data.serializers.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class AnalyzedLocationData(
    val id: Int,
    val locationLat: Double,
    val snappedLocationLat: Double,
    val locationLng: Double,
    val snappedLocationLng: Double,
    val speed: Double,
    val acceleration: Double,
    val orientation: Double,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant,
)
