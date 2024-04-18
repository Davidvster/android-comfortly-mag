package com.dv.comfortly.ui.trip.questionnaire

import androidx.lifecycle.SavedStateHandle
import com.dv.comfortly.data.models.answer.AnswerData
import com.dv.comfortly.data.raw.questionnaire.QuestionnaireSource
import com.dv.comfortly.domain.models.QuestionnaireType
import com.dv.comfortly.domain.models.TripSummary
import com.dv.comfortly.domain.usecases.LoadQuestionsUseCase
import com.dv.comfortly.domain.usecases.LoadTripsUseCase
import com.dv.comfortly.domain.usecases.StoreAnswersUseCase
import com.dv.comfortly.domain.usecases.params.QuestionnaireParams
import com.dv.comfortly.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class QuestionnaireViewModel
    @Inject
    constructor(
        private val storeAnswersUseCase: StoreAnswersUseCase,
        private val loadTripsUseCase: LoadTripsUseCase,
        private val loadQuestionsUseCase: LoadQuestionsUseCase,
        private val savedStateHandle: SavedStateHandle,
    ) : BaseViewModel<QuestionnaireState, QuestionnaireEvent>(QuestionnaireState()) {
        companion object {
            private const val MULTI_CHOICE_SEPARATOR = ";"
            private val CLEAR_LIST_DELAY = 1.seconds
        }

        private var answers = mutableListOf<AnswerData>()

        private val updatedAnswersViewState
            get() =
                viewState.copy(
                    answers = answers,
                    submitEnabled = answers.none { it.answer.isNullOrEmpty() },
                )

        private var tripId: Long = -1
        private lateinit var questionnaireType: QuestionnaireType

        fun getQuestions(
            tripId: Long,
            questionnaireType: QuestionnaireType,
        ) {
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
                            timestamp = Clock.System.now(),
                        ),
                    )
                }
                hideLoading()
                viewState = updatedAnswersViewState
            }
        }

        fun updateAnswer(
            questionId: Long,
            answerText: String,
        ) {
            answers.find { it.questionId == questionId }?.apply {
                answer = answerText
                timestamp = Clock.System.now()
                viewState =
                    viewState.copy(
                        answers = answers.toList(),
                        submitEnabled = answers.none { it.answer == null },
                    )
            }
            viewState = updatedAnswersViewState
        }

        fun updateAnswer(
            questionId: Long,
            answerText: String,
            isChecked: Boolean,
        ) {
            answers.find { it.questionId == questionId }?.apply {
                answer =
                    if (isChecked) {
                        if (answer.isNullOrEmpty()) {
                            answerText
                        } else {
                            "$answer$MULTI_CHOICE_SEPARATOR$answerText"
                        }
                    } else {
                        answer.orEmpty().replace("$MULTI_CHOICE_SEPARATOR$answerText", "").replace(answerText, "")
                    }
                timestamp = Clock.System.now()
                viewState = updatedAnswersViewState
            }
        }

        fun postAnswers() {
            launchWithBlockingLoading {
                val submitData =
                    QuestionnaireParams(
                        tripId = tripId,
                        questionnaireType = questionnaireType,
                        questions = answers,
                    )
                storeAnswersUseCase(submitData)
                emitEvent(
                    when (questionnaireType) {
                        QuestionnaireType.PRE_DEMOGRAPHIC ->
                            QuestionnaireEvent.NavigateToQuestionnaire(
                                tripId = tripId,
                                questionnaireType = QuestionnaireType.PRE_SPECIFIC,
                            )
                        QuestionnaireType.PRE_SPECIFIC ->
                            QuestionnaireEvent.NavigateToQuestionnaire(
                                tripId = tripId,
                                questionnaireType = QuestionnaireType.PRE_TRIP_PANAS,
                            )
                        QuestionnaireType.PRE_TRIP_PANAS -> QuestionnaireEvent.NavigateToSetup(tripId)
                        QuestionnaireType.POST_TRIP_PANAS ->
                            QuestionnaireEvent.NavigateToQuestionnaire(
                                tripId = tripId,
                                questionnaireType = QuestionnaireType.POST_SPECIFIC,
                            )
                        QuestionnaireType.POST_SPECIFIC -> QuestionnaireEvent.Finish()
                    },
                )
            }
        }

        fun onPrefillQuestions() {
            launchWithBlockingLoading {
                emitEvent(QuestionnaireEvent.ShowPrefillFromTrip(loadTripsUseCase().asReversed().filter { it.id != tripId }))
            }
        }

        fun onPrefillQuestionsSelected(trip: TripSummary) {
            launchWithBlockingLoading {
                val answeredQuestions = loadQuestionsUseCase(QuestionnaireParams(trip.id, questionnaireType))
                viewState =
                    viewState.copy(
                        answers = emptyList(),
                        submitEnabled = false,
                    )
                delay(CLEAR_LIST_DELAY)
                answeredQuestions.questionsWithAnswers.forEachIndexed { index, questionAnswer ->
                    answers.getOrNull(index)?.answer = questionAnswer.answer
                }
                viewState = updatedAnswersViewState
            }
        }
    }
