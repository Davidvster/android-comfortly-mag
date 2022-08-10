package com.dv.comfortly.data.raw.db.repository

import com.dv.comfortly.data.mappers.TripEcgSampleMapper
import com.dv.comfortly.data.raw.db.dao.TripEcgDatapointDao
import com.dv.comfortly.domain.models.EcgDataSample

interface TripEcgDataRepository {

    suspend fun insert(data: EcgDataSample): EcgDataSample

    class Default(
        private val tripEcgDatapointDao: TripEcgDatapointDao
    ) : TripEcgDataRepository {

        override suspend fun insert(data: EcgDataSample): EcgDataSample =
            data.copy(id = tripEcgDatapointDao.insertTripEcgDatapoint(TripEcgSampleMapper.domainToDb(data)))
    }
}
