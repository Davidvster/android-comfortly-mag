package com.dv.comfortly.data.raw.db.repository

import com.dv.comfortly.data.mappers.TripEcgCalibrationSampleMapper
import com.dv.comfortly.data.raw.db.dao.TripEcgCalibrationDatapointDao
import com.dv.comfortly.domain.models.EcgDataSample

interface TripEcgCalibrationDataRepository {

    suspend fun insert(data: EcgDataSample): EcgDataSample

    class Default(
        private val tripEcgCalibrationDatapointDao: TripEcgCalibrationDatapointDao
    ) : TripEcgCalibrationDataRepository {

        override suspend fun insert(data: EcgDataSample): EcgDataSample =
            data.copy(id = tripEcgCalibrationDatapointDao.insertTripEcgDatapoint(TripEcgCalibrationSampleMapper.domainToDb(data)))
    }
}
