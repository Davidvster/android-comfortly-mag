package com.dv.comfortly.domain.usecases.params

import com.dv.comfortly.data.models.answer.AnswerData
import com.dv.comfortly.domain.models.QuestionnaireType

data class QuestionnaireParams(
    val tripId: Long,
    val questionnaireType: QuestionnaireType,
    val questions: List<AnswerData> = emptyList(),
)
