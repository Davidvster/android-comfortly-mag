package com.dv.comfortly.domain.usecases

import com.dv.comfortly.data.raw.db.repository.TripRepository
import com.dv.comfortly.domain.models.TripSummary
import javax.inject.Inject

interface LoadTripsUseCase : BaseUseCase.Output<List<TripSummary>> {
    class Default
        @Inject
        constructor(
            private val tripRepository: TripRepository,
        ) : LoadTripsUseCase {
            override suspend operator fun invoke(): List<TripSummary> = tripRepository.loadAllTrips()
        }
}
