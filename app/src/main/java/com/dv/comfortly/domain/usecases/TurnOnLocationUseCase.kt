package com.dv.comfortly.domain.usecases

import androidx.activity.result.IntentSenderRequest
import com.dv.comfortly.domain.repositories.GpsRepository
import javax.inject.Inject

interface TurnOnLocationUseCase : BaseUseCase.Output<Pair<Boolean, IntentSenderRequest?>> {

    class Default @Inject constructor(
        private val gpsRepository: GpsRepository
    ) : TurnOnLocationUseCase {
        override suspend fun invoke(): Pair<Boolean, IntentSenderRequest?> = gpsRepository.turnOnGps()
    }
}
