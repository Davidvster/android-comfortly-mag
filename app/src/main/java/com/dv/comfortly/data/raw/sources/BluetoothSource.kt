package com.dv.comfortly.data.raw.sources

import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface BluetoothSource {
    val isBluetoothEnabled: Boolean

    class Default
        @Inject
        constructor(
            @ApplicationContext private val context: Context,
        ) : BluetoothSource {
            private val bluetoothAdapter by lazy {
                (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
            }

            override val isBluetoothEnabled: Boolean
                get() = bluetoothAdapter.isEnabled
        }
}
