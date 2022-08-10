package com.dv.comfortly.data.raw.models

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable

@Serializable
data class QuestionnaireData(
    val id: Long,
    @StringRes val question: Int,
    val type: QuestionType,
    @StringRes val possibleAnswers: List<Int>? = null
)

enum class QuestionType(private val text: String) {
    TEXT("TEXT"),
    NUMBER("NUMBER"),
    NUMBER_DECIMAL("NUMBER_DECIMAL"),
    MULTIPLE_CHOICE("MULTIPLE_CHOICE"),
    SINGLE_CHOICE("SINGLE_CHOICE")
}
