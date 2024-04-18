package com.dv.comfortly.data.raw.questionnaire

import com.dv.comfortly.R
import com.dv.comfortly.data.raw.models.QuestionType
import com.dv.comfortly.data.raw.models.QuestionnaireData
import com.dv.comfortly.domain.models.QuestionnaireType

object QuestionnaireSource {
    fun getQuestionnaire(questionnaireType: QuestionnaireType): List<QuestionnaireData> =
        when (questionnaireType) {
            QuestionnaireType.PRE_DEMOGRAPHIC -> PRE_DEMOGRAPHIC
            QuestionnaireType.PRE_SPECIFIC -> PRE_SPECIFIC
            QuestionnaireType.PRE_TRIP_PANAS -> PRE_PANAS
            QuestionnaireType.POST_TRIP_PANAS -> POST_PANAS
            QuestionnaireType.POST_SPECIFIC -> POST_SPECIFIC
        }

    private val PRE_DEMOGRAPHIC by lazy {
        listOf(
            QuestionnaireData(
                id = nextId(),
                question = R.string.demographic_1_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.demographic_1_answer_1,
                        R.string.demographic_1_answer_2,
                        R.string.demographic_1_answer_3,
                        R.string.demographic_1_answer_4,
                        R.string.demographic_1_answer_5,
                        R.string.demographic_1_answer_6,
                    ),
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.demographic_2_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.demographic_2_answer_1,
                        R.string.demographic_2_answer_2,
                        R.string.demographic_2_answer_3,
                    ),
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.demographic_3_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.demographic_3_answer_1,
                        R.string.demographic_3_answer_2,
                        R.string.demographic_3_answer_3,
                        R.string.demographic_3_answer_4,
                        R.string.demographic_3_answer_5,
                    ),
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.demographic_4_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.demographic_4_answer_1,
                        R.string.demographic_4_answer_2,
                        R.string.demographic_4_answer_3,
                        R.string.demographic_4_answer_4,
                        R.string.demographic_4_answer_5,
                        R.string.demographic_4_answer_6,
                        R.string.demographic_4_answer_7,
                    ),
            ),
        )
    }

    private val PRE_SPECIFIC by lazy {
        listOf(
            QuestionnaireData(
                id = nextId(),
                question = R.string.specific_1_question,
                type = QuestionType.MULTIPLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.specific_1_answer_1,
                        R.string.specific_1_answer_2,
                        R.string.specific_1_answer_3,
                        R.string.specific_1_answer_4,
                        R.string.specific_1_answer_5,
                        R.string.specific_1_answer_6,
                        R.string.specific_1_answer_7,
                        R.string.specific_1_answer_8,
                    ),
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.specific_2_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.specific_2_answer_1,
                        R.string.specific_2_answer_2,
                        R.string.specific_2_answer_3,
                        R.string.specific_2_answer_4,
                        R.string.specific_2_answer_5,
                        R.string.specific_2_answer_6,
                    ),
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.specific_3_question,
                type = QuestionType.MULTIPLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.specific_3_answer_1,
                        R.string.specific_3_answer_2,
                        R.string.specific_3_answer_3,
                        R.string.specific_3_answer_4,
                        R.string.specific_3_answer_5,
                        R.string.specific_3_answer_6,
                        R.string.specific_3_answer_7,
                    ),
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.specific_4_question,
                type = QuestionType.NUMBER,
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.specific_5_question,
                type = QuestionType.NUMBER,
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.specific_6_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.specific_6_answer_1,
                        R.string.specific_6_answer_2,
                    ),
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.specific_7_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.specific_7_answer_1,
                        R.string.specific_7_answer_2,
                        R.string.specific_7_answer_3,
                    ),
            ),
        )
    }

    private val PRE_PANAS by lazy {
        generatePanas()
    }

    private val POST_PANAS by lazy {
        generatePanas()
    }

    private val POST_SPECIFIC by lazy {
        listOf(
            QuestionnaireData(
                id = nextId(),
                question = R.string.post_1_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.post_1_answer_1,
                        R.string.post_1_answer_2,
                        R.string.post_1_answer_3,
                        R.string.post_1_answer_4,
                        R.string.post_1_answer_5,
                    ),
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.post_2_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.post_1_answer,
                        R.string.post_2_answer,
                        R.string.post_3_answer,
                        R.string.post_4_answer,
                        R.string.post_5_answer,
                    ),
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.post_3_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.post_1_answer,
                        R.string.post_2_answer,
                        R.string.post_3_answer,
                        R.string.post_4_answer,
                        R.string.post_5_answer,
                    ),
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.post_4_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.post_1_answer,
                        R.string.post_2_answer,
                        R.string.post_3_answer,
                        R.string.post_4_answer,
                        R.string.post_5_answer,
                    ),
            ),
            QuestionnaireData(
                id = nextId(),
                question = R.string.post_5_question,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.post_5_answer_1,
                        R.string.post_5_answer_2,
                        R.string.post_5_answer_3,
                    ),
            ),
        )
    }

    private var questionIdGenerator: Long = 0

    private fun nextId(): Long = questionIdGenerator++

    private fun generatePanas() =
        listOf(
            R.string.panas_1_title,
            R.string.panas_2_title,
            R.string.panas_3_title,
            R.string.panas_4_title,
            R.string.panas_5_title,
            R.string.panas_6_title,
            R.string.panas_7_title,
            R.string.panas_8_title,
            R.string.panas_9_title,
            R.string.panas_10_title,
            R.string.panas_11_title,
            R.string.panas_12_title,
            R.string.panas_13_title,
            R.string.panas_14_title,
            R.string.panas_15_title,
            R.string.panas_16_title,
            R.string.panas_17_title,
            R.string.panas_18_title,
            R.string.panas_19_title,
            R.string.panas_20_title,
        ).map { title ->
            QuestionnaireData(
                id = nextId(),
                question = title,
                type = QuestionType.SINGLE_CHOICE,
                possibleAnswers =
                    listOf(
                        R.string.panas_1_answer,
                        R.string.panas_2_answer,
                        R.string.panas_3_answer,
                        R.string.panas_4_answer,
                        R.string.panas_5_answer,
                    ),
            )
        }
}
