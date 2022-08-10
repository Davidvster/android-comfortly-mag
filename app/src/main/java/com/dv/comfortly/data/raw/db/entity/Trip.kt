package com.dv.comfortly.data.raw.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "trip"
)
data class Trip(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String
)

data class TripWithData(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @Relation(
        parentColumn = "id",
        entityColumn = "trip_id",
        entity = TripCalibrationDatapoint::class
    ) val calibrationDataPoints: List<TripCalibrationDatapoint>,
    @Relation(
        parentColumn = "id",
        entityColumn = "trip_id",
        entity = TripEcgCalibrationSample::class
    ) val ecgCalibrationDataPoints: List<TripEcgCalibrationSample>,
    @Relation(
        parentColumn = "id",
        entityColumn = "trip_id",
        entity = TripDatapoint::class
    ) val dataPoints: List<TripDatapoint>,
    @Relation(
        parentColumn = "id",
        entityColumn = "trip_id",
        entity = TripEcgSample::class
    ) val ecgDataPoints: List<TripEcgSample>
)
