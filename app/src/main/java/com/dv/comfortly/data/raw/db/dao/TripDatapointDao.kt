package com.dv.comfortly.data.raw.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dv.comfortly.data.raw.db.entity.TripDatapoint
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDatapointDao {
    @Insert
    suspend fun insertTripDatapoint(model: TripDatapoint): Long

    @Query("""SELECT * FROM trip_datapoint WHERE id == :tripDataPointId""")
    suspend fun loadTriDataPoint(tripDataPointId: Long): TripDatapoint

    @Query("""SELECT * FROM trip_datapoint WHERE trip_id = :tripId ORDER BY timestamp ASC""")
    suspend fun loadAllForTrip(tripId: Long): List<TripDatapoint>

    @Query("""SELECT * FROM trip_datapoint WHERE trip_id = :tripId ORDER BY timestamp ASC LIMIT 1""")
    suspend fun loadFirstForTrip(tripId: Long): TripDatapoint?

    @Query("""SELECT * FROM trip_datapoint WHERE trip_id = :tripId ORDER BY timestamp DESC LIMIT 1""")
    suspend fun loadLastForTrip(tripId: Long): TripDatapoint?

    @Query("""SELECT * FROM trip_datapoint WHERE trip_id = :tripId ORDER BY timestamp DESC LIMIT 1""")
    fun observeLastForTrip(tripId: Long): Flow<TripDatapoint>

    @Query("""SELECT * FROM trip_datapoint WHERE trip_id = :tripId ORDER BY timestamp ASC""")
    fun observeForTrip(tripId: Long): Flow<List<TripDatapoint>>

    @Delete
    suspend fun deleteTripDatapoint(model: TripDatapoint)
}
