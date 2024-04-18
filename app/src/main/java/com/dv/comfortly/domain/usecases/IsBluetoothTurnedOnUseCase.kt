package com.dv.comfortly.domain.usecases

import com.dv.comfortly.domain.repositories.BluetoothRepository
import javax.inject.Inject

interface IsBluetoothTurnedOnUseCase : BaseUseCase.Output<Boolean> {
    class Default
        @Inject
        constructor(
            private val bluetoothRepository: BluetoothRepository,
        ) : IsBluetoothTurnedOnUseCase {
            override suspend fun invoke(): Boolean = bluetoothRepository.isBluetoothEnabled
        }
}
