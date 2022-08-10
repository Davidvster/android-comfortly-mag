package com.dv.comfortly.ui.trip.questionnaire

import com.dv.comfortly.domain.models.QuestionnaireType

sealed class QuestionnaireEvent {
    data class NavigateToQuestionnaire(val tripId: Long, val questionnaireType: QuestionnaireType) : QuestionnaireEvent()
    data class NavigateToSetup(val tripId: Long) : QuestionnaireEvent()
    class Finish : QuestionnaireEvent()
}
