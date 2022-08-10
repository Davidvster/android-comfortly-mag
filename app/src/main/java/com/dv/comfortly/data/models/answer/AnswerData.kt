package com.dv.comfortly.data.models.answer

import androidx.annotation.StringRes
import com.dv.comfortly.data.raw.models.QuestionType
import com.dv.comfortly.data.serializers.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class AnswerData(
    val tripId: Long,
    val questionId: Long,
    @StringRes val question: Int,
    val type: QuestionType,
    var answer: String? = null,
    @StringRes val possibleAnswers: List<Int>? = null,
    @Serializable(with = InstantSerializer::class)
    var timestamp: Instant,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AnswerData

        if (tripId != other.tripId) return false
        if (questionId != other.questionId) return false
        if (question != other.question) return false
        if (type != other.type) return false
        if (!answer.contentEquals(other.answer)) return false
        if (possibleAnswers != other.possibleAnswers) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tripId.hashCode()
        result = 31 * result + questionId.hashCode()
        result = 31 * result + question
        result = 31 * result + type.hashCode()
        result = 31 * result + (answer?.hashCode() ?: 0)
        result = 31 * result + (possibleAnswers?.hashCode() ?: 0)
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
