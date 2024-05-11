package com.dv.comfortly.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dv.comfortly.R
import com.dv.comfortly.databinding.ActivityDashboardBinding
import com.dv.comfortly.ui.base.BaseActivity
import com.dv.comfortly.ui.base.extensions.setThrottleClickListener
import com.dv.comfortly.ui.base.viewBinding
import com.dv.comfortly.ui.ext.showDialog
import com.dv.comfortly.ui.trip.details.TripDetailsActivity
import com.dv.comfortly.ui.trip.questionnaire.QuestionnaireActivity
import com.dv.comfortly.ui.trip.recordtrip.RecordTripActivity
import com.dv.comfortly.ui.trip.recordtrip.RecordTripType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseActivity<DashboardState, DashboardEvent>() {
    companion object {
        fun newIntent(context: Context) = Intent(context, DashboardActivity::class.java)
    }

    override val viewBinding: ActivityDashboardBinding by viewBinding(ActivityDashboardBinding::inflate)
    override val viewModel: DashboardViewModel by viewModels()

    private val adapter: AnsweredTripsAdapter by lazy {
        AnsweredTripsAdapter(
            onItemClickListener = { tripId ->
                startActivity(TripDetailsActivity.newIntent(this, tripId))
            },
            onItemLongPressListener = { trip ->
                showDialog(
                    titleText = getString(R.string.delete_trip_title, trip.id, trip.name),
                    message = R.string.delete_trip_message,
                    positiveButtonText = R.string.delete,
                    positiveButtonListener = {
                        viewModel.deleteTrip(trip.id)
                    },
                    negativeButtonText = R.string.cancel,
                )
            },
        )
    }

    override fun renderState(state: DashboardState) {
        adapter.submitList(state.trips)
        if (state.trips.isNotEmpty() && state.hasNewTrip) {
            viewBinding.tripsList.post {
                viewBinding.tripsList.smoothScrollToPosition(0)
            }
        }
    }

    override fun handleEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.ToQuestionnaire -> {
                startActivity(QuestionnaireActivity.newIntent(this, event.tripId, event.questionnaireType))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewBinding) {
            swipeRefresh.setOnRefreshListener {
                viewModel.getAnalyzedTrips()
                swipeRefresh.isRefreshing = false
            }

            newTripButton.setThrottleClickListener {
                startNewTrip()
            }

            testButton.setThrottleClickListener {
                startActivity(RecordTripActivity.newIntent(this@DashboardActivity,null, RecordTripType.DEMO))
            }

            tripsList.layoutManager = LinearLayoutManager(this@DashboardActivity, RecyclerView.VERTICAL, false)
            tripsList.adapter = adapter
        }
    }

    private fun startNewTrip() {
        val tripNameInputView = EditText(this)
        AlertDialog.Builder(this)
            .setTitle(R.string.add_new_trip)
            .setMessage(R.string.input_trip_name)
            .setView(tripNameInputView)
            .setPositiveButton(R.string._continue) { _, _ ->
                viewModel.startNewTrip(tripNameInputView.text.toString())
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAnalyzedTrips()
    }
}
