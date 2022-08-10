package com.dv.comfortly.ui.dashboard

import com.dv.comfortly.domain.models.QuestionnaireType

sealed class DashboardEvent {
    data class ToQuestionnaire(val tripId: Long, val questionnaireType: QuestionnaireType) : DashboardEvent()
}
