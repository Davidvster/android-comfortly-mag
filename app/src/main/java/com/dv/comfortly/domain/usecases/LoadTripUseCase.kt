package com.dv.comfortly.domain.usecases

import com.dv.comfortly.data.raw.db.repository.TripRepository
import com.dv.comfortly.domain.models.Trip
import javax.inject.Inject

interface LoadTripUseCase : BaseUseCase.InputOutput<Long, Trip> {

    class Default @Inject constructor(
        private val tripRepository: TripRepository
    ) : LoadTripUseCase {

        override suspend operator fun invoke(input: Long): Trip = tripRepository.loadTrip(input)
    }
}
