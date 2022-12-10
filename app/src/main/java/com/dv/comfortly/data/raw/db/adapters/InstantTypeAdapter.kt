package com.dv.comfortly.data.raw.db.adapters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

internal class InstantTypeAdapter {

    @TypeConverter
    fun fromRaw(raw: Long?): Instant? = raw?.let { Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun toRaw(date: Instant?): Long? = date?.toEpochMilliseconds()
}
