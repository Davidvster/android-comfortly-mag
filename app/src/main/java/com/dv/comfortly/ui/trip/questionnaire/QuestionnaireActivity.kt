package com.dv.comfortly.ui.trip.questionnaire

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dv.comfortly.R
import com.dv.comfortly.databinding.ActivityQuestionnaireBinding
import com.dv.comfortly.domain.models.QuestionnaireType
import com.dv.comfortly.domain.models.TripSummary
import com.dv.comfortly.ui.base.BaseActivity
import com.dv.comfortly.ui.base.extensions.setThrottleClickListener
import com.dv.comfortly.ui.base.viewBinding
import com.dv.comfortly.ui.trip.setup.SetupTripActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionnaireActivity : BaseActivity<QuestionnaireState, QuestionnaireEvent>() {

    companion object {

        private const val ARG_TRIP_ID = "ARG_TRIP_ID"
        private const val ARG_QUESTIONNAIRE_TYPE = "ARG_QUESTIONNAIRE_TYPE"

        fun newIntent(context: Context, tripId: Long, questionnaireType: QuestionnaireType) =
            Intent(context, QuestionnaireActivity::class.java).apply {
                putExtra(ARG_TRIP_ID, tripId)
                putExtra(ARG_QUESTIONNAIRE_TYPE, questionnaireType)
            }
    }

    override val viewBinding: ActivityQuestionnaireBinding by viewBinding(
        ActivityQuestionnaireBinding::inflate
    )
    override val viewModel: QuestionnaireViewModel by viewModels()

    private val adapter: QuestionsAdapter by lazy {
        QuestionsAdapter(
            { questionId, answer ->
                viewModel.updateAnswer(questionId, answer)
            },
            { questionId, answer, isChecked ->
                viewModel.updateAnswer(questionId, answer, isChecked)
            }
        )
    }

    override fun renderState(state: QuestionnaireState) {
        adapter.submitList(state.answers)
        viewBinding.submitButton.isEnabled = state.submitEnabled
    }

    override fun handleEvent(event: QuestionnaireEvent) {
        when (event) {
            is QuestionnaireEvent.NavigateToQuestionnaire -> {
                startActivity(newIntent(this, event.tripId, event.questionnaireType))
                finish()
            }
            is QuestionnaireEvent.NavigateToSetup -> {
                startActivity(SetupTripActivity.newIntent(this, event.tripId))
                finish()
            }
            is QuestionnaireEvent.ShowPrefillFromTrip -> showSelectTripDialog(event.trips)
            is QuestionnaireEvent.Finish -> finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tripId = intent.getLongExtra(ARG_TRIP_ID, -1)
        val questionnaireType = intent.getSerializableExtra(ARG_QUESTIONNAIRE_TYPE) as QuestionnaireType

        initUi(questionnaireType)

        viewModel.getQuestions(tripId, questionnaireType)
    }

    private fun initUi(questionnaireType: QuestionnaireType) = with(viewBinding) {

        setToolbar(viewBinding.toolbar, questionnaireType.getToolBarTitle())
        instructions.setText(questionnaireType.getQuestionnaireInstructions())
        questionsList.layoutManager = LinearLayoutManager(this@QuestionnaireActivity, RecyclerView.VERTICAL, false)
        questionsList.adapter = adapter

        submitButton.setThrottleClickListener {
            viewModel.postAnswers()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val questionnaireType = intent.getSerializableExtra(ARG_QUESTIONNAIRE_TYPE) as QuestionnaireType
        if (questionnaireType == QuestionnaireType.PRE_DEMOGRAPHIC || questionnaireType == QuestionnaireType.PRE_SPECIFIC) {
            menuInflater.inflate(R.menu.questionnaire_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.prefill_questions -> {
            viewModel.onPrefillQuestions()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showSelectTripDialog(trips: List<TripSummary>) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.prefill_questions)
            .setItems(trips.map { "${it.id} - ${it.name} -" }.toTypedArray()) { _, which ->
                viewModel.onPrefillQuestionsSelected(trips[which])
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    @StringRes
    private fun QuestionnaireType.getToolBarTitle(): Int = when (this) {
        QuestionnaireType.PRE_TRIP_PANAS -> R.string.panas
        QuestionnaireType.PRE_SPECIFIC -> R.string.specific
        QuestionnaireType.PRE_DEMOGRAPHIC -> R.string.demographic
        QuestionnaireType.POST_TRIP_PANAS -> R.string.panas
        QuestionnaireType.POST_SPECIFIC -> R.string.specific
    }

    @StringRes
    private fun QuestionnaireType.getQuestionnaireInstructions(): Int = when (this) {
        QuestionnaireType.PRE_TRIP_PANAS -> R.string.panas_instructions
        QuestionnaireType.PRE_SPECIFIC -> R.string.pre_specific_instructions
        QuestionnaireType.PRE_DEMOGRAPHIC -> R.string.demographic
        QuestionnaireType.POST_TRIP_PANAS -> R.string.panas_instructions
        QuestionnaireType.POST_SPECIFIC -> R.string.post_specific_instructions
    }
}
