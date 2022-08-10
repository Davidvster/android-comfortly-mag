package com.dv.comfortly.domain.usecases

import android.content.Context
import com.dv.comfortly.data.raw.db.repository.QuestionAnswerRepository
import com.dv.comfortly.data.raw.db.repository.QuestionnaireRepository
import com.dv.comfortly.domain.models.QuestionAnswer
import com.dv.comfortly.domain.models.Questionnaire
import com.dv.comfortly.domain.usecases.params.QuestionnaireParams
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface StoreAnswersUseCase : BaseUseCase.Input<QuestionnaireParams> {

    class Default @Inject constructor(
        private val questionnaireRepository: QuestionnaireRepository,
        private val questionAnswerRepository: QuestionAnswerRepository,
        @ApplicationContext private val context: Context
    ) : StoreAnswersUseCase {
        override suspend fun invoke(input: QuestionnaireParams) {
            val questionnaire = questionnaireRepository.createQuestionnaire(
                Questionnaire(
                    tripId = input.tripId,
                    questionnaireType = input.questionnaireType
                )
            )
            input.questions.forEach { question ->
                questionAnswerRepository.insert(
                    QuestionAnswer(
                        questionnaireId = questionnaire.id,
                        question = context.getString(question.question),
                        answer = question.answer.orEmpty(),
                        timestamp = question.timestamp
                    )
                )
            }
        }
    }
}
