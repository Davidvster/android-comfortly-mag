package com.dv.comfortly.data.raw.db.repository

import com.dv.comfortly.data.mappers.QuestionAnswerMapper
import com.dv.comfortly.data.raw.db.dao.QuestionAnswerDao
import com.dv.comfortly.domain.models.QuestionAnswer

interface QuestionAnswerRepository {
    suspend fun insert(data: QuestionAnswer): QuestionAnswer

    class Default(
        private val questionAnswerDao: QuestionAnswerDao,
    ) : QuestionAnswerRepository {
        override suspend fun insert(data: QuestionAnswer): QuestionAnswer =
            data.copy(id = questionAnswerDao.inserQuestionAnswer(QuestionAnswerMapper.domainToDb(data)))
    }
}
