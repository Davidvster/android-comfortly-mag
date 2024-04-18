package com.dv.comfortly.ui.contracts

import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class EnableBluetoothResultContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(
        context: Context,
        input: Unit,
    ): Intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): Boolean = resultCode == RESULT_OK
}
