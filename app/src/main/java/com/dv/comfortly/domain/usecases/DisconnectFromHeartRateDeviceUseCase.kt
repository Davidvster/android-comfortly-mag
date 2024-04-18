package com.dv.comfortly.domain.usecases

import com.dv.comfortly.domain.repositories.HeartRateRepository
import javax.inject.Inject

interface DisconnectFromHeartRateDeviceUseCase : BaseUseCase.Input<String> {
    class Default
        @Inject
        constructor(
            private val heartRateRepository: HeartRateRepository,
        ) : DisconnectFromHeartRateDeviceUseCase {
            override suspend fun invoke(input: String) = heartRateRepository.disconnectDevice(input)
        }
}
