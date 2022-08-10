package com.dv.comfortly.ui.trip.questionnaire

import com.dv.comfortly.data.models.answer.AnswerData

sealed class QuestionnaireState {
    object Idle : QuestionnaireState()
    data class Answers(
        val answers: List<AnswerData>,
        val submitEnabled: Boolean
    ) : QuestionnaireState()
}
