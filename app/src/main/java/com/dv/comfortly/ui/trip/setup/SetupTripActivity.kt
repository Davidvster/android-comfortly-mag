package com.dv.comfortly.ui.trip.setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dv.comfortly.R
import com.dv.comfortly.databinding.ActivitySetupTripBinding
import com.dv.comfortly.ui.base.BaseActivity
import com.dv.comfortly.ui.base.extensions.setThrottleClickListener
import com.dv.comfortly.ui.base.viewBinding
import com.dv.comfortly.ui.contracts.EnableBluetoothResultContract
import com.dv.comfortly.ui.ext.showToast
import com.dv.comfortly.ui.trip.recordtrip.RecordTripActivity
import com.dv.comfortly.ui.trip.recordtrip.RecordTripType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetupTripActivity : BaseActivity<SetupTripState, SetupTripEvent>() {

    companion object {

        private const val ARG_TRIP_ID = "ARG_TRIP_ID"

        fun newIntent(context: Context, tripId: Long) =
            Intent(context, SetupTripActivity::class.java).apply {
                putExtra(ARG_TRIP_ID, tripId)
            }
    }

    override val viewBinding: ActivitySetupTripBinding by viewBinding(ActivitySetupTripBinding::inflate)

    override val viewModel: SetupTripViewModel by viewModels()

    private val adapter: HrDevicesAdapter by lazy {
        HrDevicesAdapter { deviceId ->
            viewModel.connectToHrDevice(deviceId)
        }
    }

    private val enableBluetoothRequest = registerForActivityResult(EnableBluetoothResultContract()) {
        viewModel.onBluetoothEnabledResult()
    }

    private val enableGpsResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
        viewModel.onGpsEnabledResult(activityResult.resultCode == RESULT_OK)
    }

    override fun renderState(state: SetupTripState) = with(viewBinding) {
        bluetoothButton.isEnabled = !state.bluetoothTurnedOn
        bluetoothButton.isGone = state.bluetoothTurnedOn
        gpsButton.isEnabled = !state.gpsTurnedOn
        gpsButton.isGone = state.gpsTurnedOn
        hrDevicesInstructions.isVisible = state.searchForDevicesEnabled
        searchDevicesButton.isVisible = state.searchForDevicesEnabled
        listTitle.text = if (state.heartRateDevices.isNotEmpty()) {
            getString(R.string.select_device_to_connect)
        } else if (state.isSearchingForHrDevices) {
            getString(R.string.searching_for_devices)
        } else null
        listTitle.isVisible = state.heartRateDevices.isNotEmpty() || state.isSearchingForHrDevices
        hrDevicesList.isInvisible = state.heartRateDevices.isEmpty()
        adapter.submitList(state.heartRateDevices.toList())
        if (state.connectedHeartRateDevice != null) {
            connectedDevice.text = getString(
                R.string.connected_device,
                state.connectedHeartRateDevice.deviceId,
                state.connectedHeartRateDevice.name,
                state.connectedHeartRateDevice.address
            )
            connectedDevice.isVisible = true
        } else {
            connectedDevice.isVisible = false
        }
        submitButton.isEnabled = state.bluetoothTurnedOn && state.gpsTurnedOn && state.connectedHeartRateDevice != null
    }

    override fun handleEvent(event: SetupTripEvent) {
        when (event) {
            is SetupTripEvent.NoHrDevicesFound -> showToast(R.string.no_hr_devices_found)
            is SetupTripEvent.TurnOnGps -> enableGpsResult.launch(event.request)
            is SetupTripEvent.NavigateToCalibrateTrip -> {
                startActivity(RecordTripActivity.newIntent(this, event.tripId, RecordTripType.TEST))
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbar(viewBinding.toolbar, R.string.setup)

        viewBinding.initUi()

        val tripId = intent.getLongExtra(ARG_TRIP_ID, -1)
        viewModel.init(tripId)
    }

    private fun ActivitySetupTripBinding.initUi() {
        hrDevicesList.layoutManager = LinearLayoutManager(this@SetupTripActivity, RecyclerView.VERTICAL, false)
        hrDevicesList.adapter = adapter
        gpsButton.setThrottleClickListener {
            viewModel.turnOnLocation()
        }
        bluetoothButton.setThrottleClickListener {
            enableBluetoothRequest.launch(Unit)
        }
        searchDevicesButton.setThrottleClickListener {
            viewModel.searchForDevices()
        }
        submitButton.setThrottleClickListener {
            viewModel.onSubmitClicked()
        }
    }
}
