package com.dv.comfortly.data.raw.db.repository

import com.dv.comfortly.data.mappers.TripDatapointMapper
import com.dv.comfortly.data.raw.db.dao.TripDatapointDao
import com.dv.comfortly.domain.models.TripDatapoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TripDatapointRepository {
    suspend fun insert(data: TripDatapoint): TripDatapoint

    fun observeLastDatapointForTrip(tripId: Long): Flow<TripDatapoint>

    class Default(
        private val tripDatapointDao: TripDatapointDao,
    ) : TripDatapointRepository {
        override suspend fun insert(data: TripDatapoint): TripDatapoint =
            data.copy(id = tripDatapointDao.insertTripDatapoint(TripDatapointMapper.domainToDb(data)))

        override fun observeLastDatapointForTrip(tripId: Long): Flow<TripDatapoint> =
            tripDatapointDao.observeLastForTrip(tripId).map { TripDatapointMapper.dbToDomain(it) }
    }
}
