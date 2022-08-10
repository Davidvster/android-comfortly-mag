package com.dv.comfortly.ui.trip.recordtrip

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.dv.comfortly.R
import com.dv.comfortly.databinding.ActivityRecordTripBinding
import com.dv.comfortly.domain.models.GpsData
import com.dv.comfortly.domain.models.QuestionnaireType
import com.dv.comfortly.ui.base.BaseActivity
import com.dv.comfortly.ui.base.extensions.setThrottleClickListener
import com.dv.comfortly.ui.base.viewBinding
import com.dv.comfortly.ui.ext.configureForApp
import com.dv.comfortly.ui.ext.showDialog
import com.dv.comfortly.ui.trip.questionnaire.QuestionnaireActivity
import com.dv.comfortly.ui.utils.SimpleLineDataSet
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration.Companion.minutes

@AndroidEntryPoint
class RecordTripActivity : BaseActivity<NewTripState, RecordTripEvent>(), OnMapReadyCallback {

    companion object {

        private const val HEART_RATE_LABEL = R.string.heart_rate
        private const val ECG_LABEL = R.string.ecg
        private const val X_LABEL = R.string.x_axis
        private const val Y_LABEL = R.string.y_axis
        private const val Z_LABEL = R.string.z_axis
        private const val SCALAR_LABEL = R.string.scalar

        private const val HEART_RATE_INDEX = 0
        private const val ECG_INDEX = 0
        private const val X_LABEL_INDEX = 0
        private const val Y_LABEL_INDEX = 1
        private const val Z_LABEL_INDEX = 2
        private const val SCALAR_LABEL_INDEX = 3

        private const val MAP_ZOOM_LEVEL = 16f

        private const val ARG_TRIP_ID = "ARG_TRIP_ID"
        private const val ARG_RECORD_TYPE = "ARG_RECORD_TYPE"

        fun newIntent(context: Context, tripId: Long, type: RecordTripType) = Intent(context, RecordTripActivity::class.java).apply {
            putExtra(ARG_TRIP_ID, tripId)
            putExtra(ARG_RECORD_TYPE, type)
        }
    }

    override val viewBinding: ActivityRecordTripBinding by viewBinding(ActivityRecordTripBinding::inflate)
    override val viewModel: RecordTripViewModel by viewModels()

    private lateinit var map: GoogleMap

    override fun renderState(state: NewTripState) {
        with(viewBinding) {
            state.accelerometer?.let { accelerometerChart.setData(it) }
            state.gravity?.let { gravityChart.setData(it) }
            state.gyroscope?.let { gyroscopeChart.setData(it) }
            state.linearAcceleration?.let { linearAccelerationChart.setData(it) }
            state.rotationVector?.let { rotationVectorChart.setData(it) }
            setMapData(state.locations)
            state.heartRate?.let { hearRateChart.setData(it) }
            state.ecgData?.let { ecgChart.setData(it) }
            calibrationProgress.isVisible = state.isForCalibration
            calibrationProgress.text = getString(
                R.string.calibrating_sensors,
                state.calibrationTime.inWholeMinutes,
                (state.calibrationTime - state.calibrationTime.inWholeMinutes.minutes).inWholeSeconds
            )
        }
    }

    override fun handleEvent(event: RecordTripEvent) {
        when (event) {
            is RecordTripEvent.NavigateToCalibrateTrip -> {
                startActivity(newIntent(this, event.tripId, RecordTripType.CALIBRATE))
                finish()
            }
            is RecordTripEvent.NavigateToRecordTrip -> {
                startActivity(newIntent(this, event.tripId, RecordTripType.RECORD))
                finish()
            }
            is RecordTripEvent.NavigateToQuestionnaire -> {
                startActivity(QuestionnaireActivity.newIntent(this, event.tripId, QuestionnaireType.POST_TRIP_PANAS))
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoading()

        (supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this)

        val recordTripType = intent.getSerializableExtra(ARG_RECORD_TYPE) as RecordTripType

        setToolbar(
            viewBinding.toolbar,
            when (recordTripType) {
                RecordTripType.TEST -> R.string.test_trip
                RecordTripType.CALIBRATE -> R.string.calibrating_trip
                RecordTripType.RECORD -> R.string.recording_trip_data
            }
        )

        viewBinding.stopTripButton.setText(
            when (recordTripType) {
                RecordTripType.TEST -> R.string._continue
                RecordTripType.CALIBRATE -> R.string.skip_calibration
                RecordTripType.RECORD -> R.string.stop_recording
            }
        )

        viewBinding.stopTripButton.setThrottleClickListener { viewModel.onStopAction() }
        viewBinding.initGraphs()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        this.map = map
        hideLoading()

        map.isMyLocationEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isCompassEnabled = true

        val tripId = intent.getLongExtra(ARG_TRIP_ID, -1)
        val recordTripType = intent.getSerializableExtra(ARG_RECORD_TYPE) as RecordTripType

        viewModel.getTripData(tripId, recordTripType)

        viewBinding.stopTripButton.isEnabled = true
    }

    private fun setMapData(locations: List<GpsData>) {
        if (locations.size > 1) {
            val first = locations[locations.lastIndex - 1]
            val second = locations.last()
            map.addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(first.latitude, first.longitude),
                        LatLng(second.latitude, second.longitude)
                    )
                    .width(8f)
                    .color(ContextCompat.getColor(this, R.color.green))
            )
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(second.latitude, second.longitude),
                    MAP_ZOOM_LEVEL
                )
            )
        } else if (locations.size == 1) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(locations.first().latitude, locations.first().longitude),
                    MAP_ZOOM_LEVEL
                )
            )
        }
    }

    private fun ActivityRecordTripBinding.initGraphs() {
        accelerometerChart.initData(R.string.accelerometer)
        gravityChart.initData(R.string.gravity)
        gyroscopeChart.initData(R.string.gyroscope)
        linearAccelerationChart.initData(R.string.linear_acceleration)

        rotationVectorChart.apply {
            val x = SimpleLineDataSet(dataLabel = getString(X_LABEL), lineColor = Color.RED)
            val y = SimpleLineDataSet(dataLabel = getString(Y_LABEL), lineColor = Color.GREEN)
            val z = SimpleLineDataSet(dataLabel = getString(Z_LABEL), lineColor = Color.BLUE)
            val scalar = SimpleLineDataSet(dataLabel = getString(SCALAR_LABEL), lineColor = Color.YELLOW)
            configureForApp(getString(R.string.rotation_vector))
            data = LineData(x, y, z, scalar)
        }

        hearRateChart.apply {
            val x = SimpleLineDataSet(dataLabel = getString(HEART_RATE_LABEL), lineColor = Color.RED)
            configureForApp(getString(R.string.heart_rate))
            data = LineData(x)
        }

        ecgChart.apply {
            val x = SimpleLineDataSet(dataLabel = getString(ECG_LABEL), lineColor = Color.RED)
            configureForApp(getString(R.string.ecg))
            data = LineData(x)
        }
    }

    private fun LineChart.initData(@StringRes title: Int) {
        val x = SimpleLineDataSet(dataLabel = getString(X_LABEL), lineColor = Color.RED)
        val y = SimpleLineDataSet(dataLabel = getString(Y_LABEL), lineColor = Color.GREEN)
        val z = SimpleLineDataSet(dataLabel = getString(Z_LABEL), lineColor = Color.BLUE)
        configureForApp(getString(title))
        data = LineData(x, y, z)
    }

    private fun LineChart.setData(newData: GraphData) {
        val x: LineDataSet = data.getDataSetByIndex(X_LABEL_INDEX) as LineDataSet
        val y: LineDataSet = data.getDataSetByIndex(Y_LABEL_INDEX) as LineDataSet
        val z: LineDataSet = data.getDataSetByIndex(Z_LABEL_INDEX) as LineDataSet
        x.values = newData.xAxis
        y.values = newData.yAxis
        z.values = newData.zAxis
        x.notifyDataSetChanged()
        y.notifyDataSetChanged()
        z.notifyDataSetChanged()
        data.notifyDataChanged()
        notifyDataSetChanged()
        invalidate()
    }

    private fun LineChart.setData(newData: RotationVectorGraphData) {
        val x: LineDataSet = data.getDataSetByIndex(X_LABEL_INDEX) as LineDataSet
        val y: LineDataSet = data.getDataSetByIndex(Y_LABEL_INDEX) as LineDataSet
        val z: LineDataSet = data.getDataSetByIndex(Z_LABEL_INDEX) as LineDataSet
        val scalar: LineDataSet = data.getDataSetByIndex(SCALAR_LABEL_INDEX) as LineDataSet
        x.values = newData.xAxis
        y.values = newData.yAxis
        z.values = newData.zAxis
        scalar.values = newData.scalar
        x.notifyDataSetChanged()
        y.notifyDataSetChanged()
        z.notifyDataSetChanged()
        scalar.notifyDataSetChanged()
        data.notifyDataChanged()
        notifyDataSetChanged()
        invalidate()
    }

    private fun LineChart.setData(newData: HeartRateGraphData) {
        val x: LineDataSet = data.getDataSetByIndex(HEART_RATE_INDEX) as LineDataSet
        x.values = newData.heartRate
        x.notifyDataSetChanged()
        data.notifyDataChanged()
        notifyDataSetChanged()
        invalidate()
    }

    private fun LineChart.setData(newData: EcgGraphData) {
        val x: LineDataSet = data.getDataSetByIndex(ECG_INDEX) as LineDataSet
        x.values = newData.ecg
        x.notifyDataSetChanged()
        data.notifyDataChanged()
        notifyDataSetChanged()
        invalidate()
    }

    override fun onBackPressed() {
        showDialog(
            title = R.string.stop_recording_trip_title,
            message = R.string.stop_recording_trip_message,
            positiveButtonText = R.string.stop_recording,
            negativeButtonText = R.string.cancel,
            positiveButtonListener = { viewModel.onStopAction() }
        )
    }
}
