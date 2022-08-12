package com.dv.comfortly.domain.usecases

import com.dv.comfortly.data.raw.db.repository.QuestionnaireRepository
import com.dv.comfortly.domain.models.Questionnaire
import com.dv.comfortly.domain.usecases.params.QuestionnaireParams
import javax.inject.Inject

interface LoadQuestionsUseCase : BaseUseCase.InputOutput<QuestionnaireParams, Questionnaire> {

    class Default @Inject constructor(
        private val questionnaireRepository: QuestionnaireRepository,
    ) : LoadQuestionsUseCase {
        override suspend fun invoke(input: QuestionnaireParams): Questionnaire {
            val questionnaires = questionnaireRepository.loadQuestionnairesForTripId(input.tripId)
            return questionnaires.first { it.questionnaireType == input.questionnaireType }
        }
    }
}
