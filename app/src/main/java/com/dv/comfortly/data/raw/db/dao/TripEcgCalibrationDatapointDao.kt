package com.dv.comfortly.data.raw.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dv.comfortly.data.raw.db.entity.TripEcgCalibrationSample

@Dao
interface TripEcgCalibrationDatapointDao {
    @Insert
    suspend fun insertTripEcgDatapoint(model: TripEcgCalibrationSample): Long

    @Query("""SELECT * FROM trip_ecg_calibration_sample WHERE trip_id = :tripId ORDER BY timestamp ASC""")
    suspend fun loadAllForTrip(tripId: Long): List<TripEcgCalibrationSample>

    @Delete
    suspend fun deleteTripDatapoint(model: TripEcgCalibrationSample)
}
