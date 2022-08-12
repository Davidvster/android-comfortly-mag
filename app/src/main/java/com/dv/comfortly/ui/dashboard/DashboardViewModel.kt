package com.dv.comfortly.ui.dashboard

import androidx.lifecycle.SavedStateHandle
import com.dv.comfortly.domain.models.QuestionnaireType
import com.dv.comfortly.domain.usecases.CreateNewTripUseCase
import com.dv.comfortly.domain.usecases.DeleteTripUseCase
import com.dv.comfortly.domain.usecases.LoadTripsUseCase
import com.dv.comfortly.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val newTripUseCase: CreateNewTripUseCase,
    private val loadTripsUseCase: LoadTripsUseCase,
    private val deleteTripsUseCase: DeleteTripUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<DashboardState, DashboardEvent>(DashboardState.Idle) {

    fun getAnalyzedTrips() {
        launchWithBlockingLoading {
            val trips = loadTripsUseCase()
            viewState = DashboardState.Trips(trips = trips.asReversed())
        }
    }

    fun startNewTrip(tripName: String) {
        launchWithBlockingLoading {
            val createdTrip = newTripUseCase(tripName)
            emitEvent(DashboardEvent.ToQuestionnaire(createdTrip.id, QuestionnaireType.PRE_DEMOGRAPHIC))
        }
    }

    fun deleteTrip(tripId: Long) {
        launchWithBlockingLoading {
            deleteTripsUseCase(tripId)
            getAnalyzedTrips()
        }
    }
}
