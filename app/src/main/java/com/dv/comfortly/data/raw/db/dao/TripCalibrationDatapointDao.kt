package com.dv.comfortly.data.raw.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dv.comfortly.data.raw.db.entity.TripCalibrationDatapoint
import kotlinx.coroutines.flow.Flow

@Dao
interface TripCalibrationDatapointDao {
    @Insert
    suspend fun insertTripCalibrationDatapoint(model: TripCalibrationDatapoint): Long

    @Query("""SELECT * FROM trip_calibration_datapoint WHERE id == :tripCalibrationDataPointId""")
    suspend fun loadTripCalinratopnDatapoint(tripCalibrationDataPointId: Long): TripCalibrationDatapoint

    @Query("""SELECT * FROM trip_calibration_datapoint WHERE trip_id = :trip_id ORDER BY timestamp ASC""")
    suspend fun loadAllForTrip(trip_id: Long): List<TripCalibrationDatapoint>

    @Query("""SELECT * FROM trip_calibration_datapoint WHERE trip_id = :trip_id ORDER BY timestamp DESC LIMIT 1""")
    fun observeLastForTrip(trip_id: Long): Flow<TripCalibrationDatapoint>

    @Query("""SELECT * FROM trip_calibration_datapoint WHERE trip_id = :trip_id ORDER BY timestamp ASC""")
    fun observeForTrip(trip_id: Long): Flow<List<TripCalibrationDatapoint>>

    @Delete
    suspend fun deleteTripCalibrationDatapoint(model: TripCalibrationDatapoint)
}
