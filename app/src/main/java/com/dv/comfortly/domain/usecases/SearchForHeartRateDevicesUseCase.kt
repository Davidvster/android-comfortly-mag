package com.dv.comfortly.domain.usecases

import com.dv.comfortly.domain.models.HeartRateDevice
import com.dv.comfortly.domain.repositories.HeartRateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SearchForHeartRateDevicesUseCase : BaseFlowUseCase.Output<HeartRateDevice> {
    class Default
        @Inject
        constructor(
            private val heartRateRepository: HeartRateRepository,
        ) : SearchForHeartRateDevicesUseCase {
            override fun invoke(): Flow<HeartRateDevice> = heartRateRepository.searchDevices()
        }
}
