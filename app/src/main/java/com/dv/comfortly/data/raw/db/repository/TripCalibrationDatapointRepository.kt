package com.dv.comfortly.data.raw.db.repository

import com.dv.comfortly.data.mappers.TripCalibrationDatapointMapper
import com.dv.comfortly.data.raw.db.dao.TripCalibrationDatapointDao
import com.dv.comfortly.domain.models.TripDatapoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TripCalibrationDatapointRepository {
    suspend fun insert(data: TripDatapoint): TripDatapoint

    fun observeLastDatapointForTrip(tripId: Long): Flow<TripDatapoint>

    class Default(
        private val tripCalibrationDatapointDao: TripCalibrationDatapointDao,
    ) : TripCalibrationDatapointRepository {
        override suspend fun insert(data: TripDatapoint): TripDatapoint =
            tripCalibrationDatapointDao.insertTripCalibrationDatapoint(TripCalibrationDatapointMapper.domainToDb(data)).let { insertedId ->
                data.copy(id = insertedId)
            }

        override fun observeLastDatapointForTrip(tripId: Long): Flow<TripDatapoint> =
            tripCalibrationDatapointDao.observeLastForTrip(tripId).map { TripCalibrationDatapointMapper.dbToDomain(it) }
    }
}
