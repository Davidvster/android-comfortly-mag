package com.dv.comfortly.data.mappers

import com.dv.comfortly.domain.models.QuestionnaireType

private typealias DbQuestionnaireType = com.dv.comfortly.data.raw.db.entity.QuestionnaireType

object QuestionnaireTypeMapper {

    fun dbToDomain(data: DbQuestionnaireType): QuestionnaireType = when (data) {
        DbQuestionnaireType.PRE_DEMOGRAPHIC -> QuestionnaireType.PRE_DEMOGRAPHIC
        DbQuestionnaireType.PRE_SPECIFIC -> QuestionnaireType.PRE_SPECIFIC
        DbQuestionnaireType.PRE_TRIP_PANAS -> QuestionnaireType.PRE_TRIP_PANAS
        DbQuestionnaireType.POST_TRIP_PANAS -> QuestionnaireType.POST_TRIP_PANAS
        DbQuestionnaireType.POST_SPECIFIC -> QuestionnaireType.POST_SPECIFIC
    }

    fun domainToDb(data: QuestionnaireType): DbQuestionnaireType = when (data) {
        QuestionnaireType.PRE_DEMOGRAPHIC -> DbQuestionnaireType.PRE_DEMOGRAPHIC
        QuestionnaireType.PRE_SPECIFIC -> DbQuestionnaireType.PRE_SPECIFIC
        QuestionnaireType.PRE_TRIP_PANAS -> DbQuestionnaireType.PRE_TRIP_PANAS
        QuestionnaireType.POST_TRIP_PANAS -> DbQuestionnaireType.POST_TRIP_PANAS
        QuestionnaireType.POST_SPECIFIC -> DbQuestionnaireType.POST_SPECIFIC
    }
}
