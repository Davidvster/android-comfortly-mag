package com.dv.comfortly.data.mappers

import com.dv.comfortly.domain.models.Questionnaire

private typealias DbQuestionnaireWithQuestions = com.dv.comfortly.data.raw.db.entity.QuestionnaireWithQuestions
private typealias DbQuestionnaire = com.dv.comfortly.data.raw.db.entity.Questionnaire

object QuestionnaireMapper {

    fun dbToDomain(data: DbQuestionnaireWithQuestions): Questionnaire = Questionnaire(
        id = data.id,
        tripId = data.tripId,
        questionnaireType = QuestionnaireTypeMapper.dbToDomain(data.questionnaireType),
        questionsWithAnswers = data.questionsWithAnswers.map { QuestionAnswerMapper.dbToDomain(it) }
    )

    fun domainToDb(data: Questionnaire): DbQuestionnaire = DbQuestionnaire(
        id = data.id,
        tripId = data.tripId,
        questionnaireType = QuestionnaireTypeMapper.domainToDb(data.questionnaireType)
    )
}
