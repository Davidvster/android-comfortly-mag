package com.dv.comfortly.data.raw.db.repository

import com.dv.comfortly.data.mappers.TripMapper
import com.dv.comfortly.data.raw.db.dao.TripDao
import com.dv.comfortly.data.raw.db.dao.TripDatapointDao
import com.dv.comfortly.domain.models.Trip
import com.dv.comfortly.domain.models.TripSummary

typealias DbTrip = com.dv.comfortly.data.raw.db.entity.Trip

interface TripRepository {

    suspend fun createTrip(name: String): Trip

    suspend fun loadTrip(tripId: Long): Trip

    suspend fun loadAllTrips(): List<TripSummary>

    suspend fun deleteTrip(tripId: Long)

    class Default(
        private val tripDao: TripDao,
        private val tripDatapointDao: TripDatapointDao
    ) : TripRepository {
        override suspend fun createTrip(name: String): Trip = Trip(
            id = tripDao.createTrip(DbTrip(name = name)),
            name = name
        )

        override suspend fun loadTrip(tripId: Long): Trip = TripMapper.dbToDomain(tripDao.loadTrip(tripId))

        override suspend fun loadAllTrips(): List<TripSummary> =
            tripDao.loadAll().map { trip ->
                val updatedTrip = if (trip.startDate == null || trip.endDate == null) {
                    val firstDataPointDate = tripDatapointDao.loadFirstForTrip(trip.id)?.timestamp
                    val lastDatapointDate = tripDatapointDao.loadLastForTrip(trip.id)?.timestamp
                    trip.copy(startDate = firstDataPointDate, endDate = lastDatapointDate).also {
                        tripDao.updateTrip(it)
                    }
                } else trip
                TripSummary(
                    id = updatedTrip.id,
                    name = updatedTrip.name,
                    startTime = updatedTrip.startDate,
                    endTime = updatedTrip.endDate
                )
            }

        override suspend fun deleteTrip(tripId: Long) = tripDao.deleteTrip(tripId)
    }
}
