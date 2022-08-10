package com.dv.comfortly.data.raw.db.adapters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

internal class InstantTypeAdapter {

    @TypeConverter
    fun fromRaw(raw: Long): Instant = Instant.fromEpochMilliseconds(raw)

    @TypeConverter
    fun toRaw(date: Instant): Long = date.toEpochMilliseconds()
}
