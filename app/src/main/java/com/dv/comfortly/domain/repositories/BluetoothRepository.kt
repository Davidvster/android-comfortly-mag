package com.dv.comfortly.domain.repositories

import com.dv.comfortly.data.raw.interactors.BluetoothTurnedOnInteractor
import javax.inject.Inject

interface BluetoothRepository {

    val isBluetoothEnabled: Boolean

    class Default @Inject constructor(
        private val bluetoothTurnedOnInteractor: BluetoothTurnedOnInteractor
    ) : BluetoothRepository {
        override val isBluetoothEnabled: Boolean
            get() = bluetoothTurnedOnInteractor.isBluetoothEnabled
    }
}
