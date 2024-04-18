package com.dv.comfortly.data.mappers

import com.dv.comfortly.domain.models.QuestionAnswer

private typealias DbQuestionAnswer = com.dv.comfortly.data.raw.db.entity.QuestionAnswer

object QuestionAnswerMapper {
    fun dbToDomain(data: DbQuestionAnswer): QuestionAnswer =
        QuestionAnswer(
            id = data.id,
            questionnaireId = data.questionnaireId,
            question = data.question,
            answer = data.answer,
            timestamp = data.timestamp,
        )

    fun domainToDb(data: QuestionAnswer): DbQuestionAnswer =
        DbQuestionAnswer(
            id = data.id,
            questionnaireId = data.questionnaireId,
            question = data.question,
            answer = data.answer,
            timestamp = data.timestamp,
        )
}
