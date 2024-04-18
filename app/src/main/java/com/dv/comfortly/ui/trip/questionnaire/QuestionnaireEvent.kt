package com.dv.comfortly.ui.trip.questionnaire

import com.dv.comfortly.domain.models.QuestionnaireType
import com.dv.comfortly.domain.models.TripSummary

sealed class QuestionnaireEvent {
    data class NavigateToQuestionnaire(val tripId: Long, val questionnaireType: QuestionnaireType) : QuestionnaireEvent()

    data class NavigateToSetup(val tripId: Long) : QuestionnaireEvent()

    data class ShowPrefillFromTrip(val trips: List<TripSummary>) : QuestionnaireEvent()

    class Finish : QuestionnaireEvent()
}
