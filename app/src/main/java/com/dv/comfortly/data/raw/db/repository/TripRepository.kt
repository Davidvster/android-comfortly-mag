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
                val firstDataPoint = tripDatapointDao.loadFirstForTrip(trip.id)
                val lastDatapoint = tripDatapointDao.loadLastForTrip(trip.id)
                TripSummary(
                    id = trip.id,
                    name = trip.name,
                    startTime = firstDataPoint?.timestamp,
                    endTime = lastDatapoint?.timestamp
                )
            }

        override suspend fun deleteTrip(tripId: Long) = tripDao.deleteTrip(tripId)
    }
}
