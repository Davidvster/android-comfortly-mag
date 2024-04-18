package com.dv.comfortly.data.raw.interactors

import com.dv.comfortly.data.raw.sources.BluetoothSource
import javax.inject.Inject

interface BluetoothTurnedOnInteractor {
    val isBluetoothEnabled: Boolean

    class Default
        @Inject
        constructor(
            private val bluetoothSource: BluetoothSource,
        ) : BluetoothTurnedOnInteractor {
            override val isBluetoothEnabled: Boolean
                get() = bluetoothSource.isBluetoothEnabled
        }
}
