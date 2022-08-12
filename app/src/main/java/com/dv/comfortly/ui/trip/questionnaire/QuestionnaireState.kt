package com.dv.comfortly.ui.trip.questionnaire

import com.dv.comfortly.data.models.answer.AnswerData

data class QuestionnaireState(
    val answers: List<AnswerData> = emptyList(),
    val submitEnabled: Boolean = false
)
