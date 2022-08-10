package com.dv.comfortly.data.models.analyzedtrip

import com.dv.comfortly.data.serializers.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class AnalyzedTripData(
    val id: Int,
    val userId: String,
    val tripId: Integer,
    val startLocationName: String,
    val startLocationLat: Double,
    val startLocationLng: Double,
    val endLocationName: String,
    val endLocationLat: Double,
    val endLocationLng: Double,
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant,
    val averageSpeed: Double,
    val maxSpeed: Double,
    val averageAcceleration: Double,
    val maxAcceleration: Double,
    val distance: Double,
    val comfortLevel: Double,
    val emotions: EmotionLevel,
    val locations: List<AnalyzedLocationData>,
)
