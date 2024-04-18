package com.dv.comfortly.data.raw.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.dv.comfortly.data.raw.db.adapters.InstantTypeAdapter
import kotlinx.datetime.Instant

@Entity(
    tableName = "trip",
)
@TypeConverters(InstantTypeAdapter::class)
data class Trip(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "start_date") val startDate: Instant? = null,
    @ColumnInfo(name = "end_date") val endDate: Instant? = null,
)

data class TripWithData(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @Relation(
        parentColumn = "id",
        entityColumn = "trip_id",
        entity = TripCalibrationDatapoint::class,
    ) val calibrationDataPoints: List<TripCalibrationDatapoint>,
    @Relation(
        parentColumn = "id",
        entityColumn = "trip_id",
        entity = TripEcgCalibrationSample::class,
    ) val ecgCalibrationDataPoints: List<TripEcgCalibrationSample>,
    @Relation(
        parentColumn = "id",
        entityColumn = "trip_id",
        entity = TripDatapoint::class,
    ) val dataPoints: List<TripDatapoint>,
    @Relation(
        parentColumn = "id",
        entityColumn = "trip_id",
        entity = TripEcgSample::class,
    ) val ecgDataPoints: List<TripEcgSample>,
)
