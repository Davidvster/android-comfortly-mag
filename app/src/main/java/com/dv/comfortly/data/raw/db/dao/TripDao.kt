package com.dv.comfortly.data.raw.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dv.comfortly.data.raw.db.entity.Trip
import com.dv.comfortly.data.raw.db.entity.TripWithData

@Dao
interface TripDao {

    @Insert
    suspend fun createTrip(model: Trip): Long

    @Query("""SELECT * FROM trip WHERE id == :tripId""")
    suspend fun loadTrip(tripId: Long): TripWithData

    @Query("""SELECT * FROM trip""")
    suspend fun loadAll(): List<Trip>

    @Query("""DELETE FROM trip WHERE id == :tripId""")
    suspend fun deleteTrip(tripId: Long)
}
