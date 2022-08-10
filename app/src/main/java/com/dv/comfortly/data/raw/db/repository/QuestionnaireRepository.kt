package com.dv.comfortly.data.raw.db.repository

import com.dv.comfortly.data.mappers.QuestionnaireMapper
import com.dv.comfortly.data.raw.db.dao.QuestionnaireDao
import com.dv.comfortly.domain.models.Questionnaire

interface QuestionnaireRepository {

    suspend fun createQuestionnaire(questionnaire: Questionnaire): Questionnaire

    suspend fun loadQuestionnaire(questionnaireId: Long): Questionnaire

    suspend fun loadQuestionnairesForTripId(tripId: Long): List<Questionnaire>

    suspend fun loadAllQuestionnaires(): List<Questionnaire>

    class Default(
        private val questionnaireDao: QuestionnaireDao
    ) : QuestionnaireRepository {
        override suspend fun createQuestionnaire(questionnaire: Questionnaire): Questionnaire =
            questionnaire.copy(id = questionnaireDao.createQuestionnaire(QuestionnaireMapper.domainToDb(questionnaire)))

        override suspend fun loadQuestionnaire(questionnaireId: Long): Questionnaire =
            QuestionnaireMapper.dbToDomain(questionnaireDao.loadQuestionnaire(questionnaireId))

        override suspend fun loadQuestionnairesForTripId(tripId: Long): List<Questionnaire> =
            questionnaireDao.loadQuestionnaireForTrip(tripId).map {
                QuestionnaireMapper.dbToDomain(it)
            }

        override suspend fun loadAllQuestionnaires(): List<Questionnaire> =
            questionnaireDao.loadAll().map { QuestionnaireMapper.dbToDomain(it) }
    }
}
