package com.dv.comfortly.domain.models

import kotlinx.datetime.Instant

data class Questionnaire(
    val id: Long = 0,
    val tripId: Long,
    val questionnaireType: QuestionnaireType,
    val questionsWithAnswers: List<QuestionAnswer> = emptyList()
)

enum class QuestionnaireType {
    PRE_DEMOGRAPHIC,
    PRE_TRIP_PANAS,
    POST_TRIP_PANAS,
}

data class QuestionAnswer(
    val id: Long = 0,
    val questionnaireId: Long,
    val question: String,
    val answer: String,
    val timestamp: Instant
)
