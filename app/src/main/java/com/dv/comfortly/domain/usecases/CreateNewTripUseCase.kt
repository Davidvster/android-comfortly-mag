package com.dv.comfortly.domain.usecases

import com.dv.comfortly.data.raw.db.repository.TripRepository
import com.dv.comfortly.domain.models.Trip
import javax.inject.Inject

interface CreateNewTripUseCase : BaseUseCase.InputOutput<String, Trip> {
    class Default
        @Inject
        constructor(
            private val tripRepository: TripRepository,
        ) : CreateNewTripUseCase {
            override suspend operator fun invoke(input: String): Trip = tripRepository.createTrip(input)
        }
}
