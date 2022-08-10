package com.dv.comfortly.data.raw.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dv.comfortly.data.raw.db.entity.TripEcgSample

@Dao
interface TripEcgDatapointDao {

    @Insert
    suspend fun insertTripEcgDatapoint(model: TripEcgSample): Long

    @Query("""SELECT * FROM trip_ecg_sample WHERE trip_id = :tripId ORDER BY timestamp ASC""")
    suspend fun loadAllForTrip(tripId: Long): List<TripEcgSample>

    @Delete
    suspend fun deleteTripDatapoint(model: TripEcgSample)
}
