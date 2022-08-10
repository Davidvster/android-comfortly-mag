package com.dv.comfortly.ui.trip.questionnaire

import androidx.lifecycle.SavedStateHandle
import com.dv.comfortly.data.models.answer.AnswerData
import com.dv.comfortly.data.raw.questionnaire.QuestionnaireSource
import com.dv.comfortly.domain.models.QuestionnaireType
import com.dv.comfortly.domain.usecases.StoreAnswersUseCase
import com.dv.comfortly.domain.usecases.params.QuestionnaireParams
import com.dv.comfortly.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class QuestionnaireViewModel @Inject constructor(
    private val storeAnswersUseCase: StoreAnswersUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<QuestionnaireState, QuestionnaireEvent>(QuestionnaireState.Idle) {

    companion object {
        private const val MULTI_CHOICE_SEPARATOR = ";"
    }

    private val answers = mutableListOf<AnswerData>()

    private var tripId: Long = -1
    private lateinit var questionnaireType: QuestionnaireType

    fun getQuestions(tripId: Long, questionnaireType: QuestionnaireType) {
        this.tripId = tripId
        this.questionnaireType = questionnaireType
        launch {
            showLoading()
            val questions = QuestionnaireSource.getQuestionnaire(questionnaireType)
            questions.forEach { question ->
                answers.add(
                    AnswerData(
                        tripId = tripId,
                        questionId = question.id,
                        question = question.question,
                        possibleAnswers = question.possibleAnswers,
                        type = question.type,
                        timestamp = Clock.System.now()
                    )
                )
            }
            hideLoading()
            viewState = QuestionnaireState.Answers(
                answers = answers.toList(),
                submitEnabled = answers.none { it.answer == null }
            )
        }
    }

    fun updateAnswer(questionId: Long, answerText: String) {
        answers.find { it.questionId == questionId }?.apply {
            answer = answerText
            timestamp = Clock.System.now()
            viewState = QuestionnaireState.Answers(
                answers = answers.toList(),
                submitEnabled = answers.none { it.answer == null }
            )
        }
    }

    fun updateAnswer(questionId: Long, answerText: String, isChecked: Boolean) {
        answers.find { it.questionId == questionId }?.apply {
            answer = if (isChecked) {
                if (answer.isNullOrEmpty()) {
                    answerText
                } else {
                    "$answer$MULTI_CHOICE_SEPARATOR$answerText"
                }
            } else {
                answer.orEmpty().replace("$MULTI_CHOICE_SEPARATOR$answerText", "").replace(answerText, "")
            }
            timestamp = Clock.System.now()
            viewState = QuestionnaireState.Answers(
                answers = answers.toList(),
                submitEnabled = answers.none { it.answer.isNullOrEmpty() }
            )
        }
    }

    fun postAnswers() {
        launchWithBlockingLoading {
            val submitData = QuestionnaireParams(
                tripId = tripId,
                questionnaireType = questionnaireType,
                questions = answers.toList()
            )
            storeAnswersUseCase(submitData)
            emitEvent(
                when (questionnaireType) {
                    QuestionnaireType.PRE_DEMOGRAPHIC -> QuestionnaireEvent.NavigateToQuestionnaire(
                        tripId = tripId,
                        questionnaireType = QuestionnaireType.PRE_TRIP_PANAS
                    )
                    QuestionnaireType.PRE_TRIP_PANAS -> QuestionnaireEvent.NavigateToSetup(tripId)
                    QuestionnaireType.POST_TRIP_PANAS -> QuestionnaireEvent.Finish()
                }
            )
        }
    }
}
