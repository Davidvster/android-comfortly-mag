package com.dv.comfortly.domain.usecases

import com.dv.comfortly.domain.repositories.HeartRateRepository
import javax.inject.Inject

interface ConnectToHeartRateDeviceUseCase : BaseUseCase.Input<String> {

    class Default @Inject constructor(
        private val heartRateRepository: HeartRateRepository
    ) : ConnectToHeartRateDeviceUseCase {
        override suspend fun invoke(input: String) = heartRateRepository.connectDevice(input)
    }
}
