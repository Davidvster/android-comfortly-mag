package com.dv.comfortly.domain.usecases

import com.dv.comfortly.data.raw.db.repository.TripRepository
import javax.inject.Inject

interface DeleteTripUseCase : BaseUseCase.Input<Long> {
    class Default
        @Inject
        constructor(
            private val tripRepository: TripRepository,
        ) : DeleteTripUseCase {
            override suspend operator fun invoke(input: Long) = tripRepository.deleteTrip(input)
        }
}
