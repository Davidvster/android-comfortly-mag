package com.dv.comfortly.data.raw.db.adapters

import androidx.room.TypeConverter
import com.dv.comfortly.data.raw.db.entity.QuestionnaireType

internal class QuestionnaireTypeAdapter {
    companion object {
        private const val PRE_DEMOGRAPHIC = "PRE_DEMOGRAPHIC"
        private const val PRE_SPECIFIC = "PRE_SPECIFIC"
        private const val PRE_MSSQ_1 = "PRE_MSSQ_1"
        private const val PRE_MSSQ_2 = "PRE_MSSQ_2"
        private const val PRE_BSSS = "PRE_BSSS"
        private const val PRE_TRIP_PANAS = "PRE_TRIP_PANAS"
        private const val POST_TRIP_PANAS = "POST_PANAS"
        private const val POST_SPECIFIC = "POST_SPECIFIC"
    }

    @TypeConverter
    fun fromRaw(raw: String): QuestionnaireType =
        when (raw) {
            PRE_DEMOGRAPHIC -> QuestionnaireType.PRE_DEMOGRAPHIC
            PRE_SPECIFIC -> QuestionnaireType.PRE_SPECIFIC
            PRE_MSSQ_1 -> QuestionnaireType.PRE_MSSQ_1
            PRE_MSSQ_2 -> QuestionnaireType.PRE_MSSQ_2
            PRE_BSSS -> QuestionnaireType.PRE_BSSS
            PRE_TRIP_PANAS -> QuestionnaireType.PRE_TRIP_PANAS
            POST_TRIP_PANAS -> QuestionnaireType.POST_TRIP_PANAS
            POST_SPECIFIC -> QuestionnaireType.POST_SPECIFIC
            else -> throw IllegalArgumentException("Unknown questionnaire type for type $raw")
        }

    @TypeConverter
    fun toRaw(type: QuestionnaireType): String =
        when (type) {
            QuestionnaireType.PRE_DEMOGRAPHIC -> PRE_DEMOGRAPHIC
            QuestionnaireType.PRE_SPECIFIC -> PRE_SPECIFIC
            QuestionnaireType.PRE_MSSQ_1 -> PRE_MSSQ_1
            QuestionnaireType.PRE_MSSQ_2 -> PRE_MSSQ_2
            QuestionnaireType.PRE_BSSS -> PRE_BSSS
            QuestionnaireType.PRE_TRIP_PANAS -> PRE_TRIP_PANAS
            QuestionnaireType.POST_TRIP_PANAS -> POST_TRIP_PANAS
            QuestionnaireType.POST_SPECIFIC -> POST_SPECIFIC
        }
}
