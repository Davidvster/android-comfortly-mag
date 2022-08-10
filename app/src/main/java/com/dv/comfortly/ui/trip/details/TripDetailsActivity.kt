package com.dv.comfortly.ui.trip.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.dv.comfortly.R
import com.dv.comfortly.databinding.ActivityTripDetailsBinding
import com.dv.comfortly.domain.models.GpsData
import com.dv.comfortly.ui.base.BaseActivity
import com.dv.comfortly.ui.base.viewBinding
import com.dv.comfortly.ui.ext.configureForApp
import com.dv.comfortly.ui.trip.recordtrip.EcgGraphData
import com.dv.comfortly.ui.trip.recordtrip.GraphData
import com.dv.comfortly.ui.trip.recordtrip.HeartRateGraphData
import com.dv.comfortly.ui.trip.recordtrip.RotationVectorGraphData
import com.dv.comfortly.ui.utils.SimpleLineDataSet
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class TripDetailsActivity : BaseActivity<TripDetailsState, TripDetailsEvent>(), OnMapReadyCallback {

    companion object {

        private const val ARG_TRIP_ID = "ARG_TRIP_ID"

        private const val TRIP_DATE_FORMAT = "HH:mm - dd.MM.yyyy"

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

        private const val ALL_MIME_TYPE = "*/*"

        private const val MAP_ZOOM_LEVEL = 10f

        fun newIntent(context: Context, tripId: Long) = Intent(context, TripDetailsActivity::class.java).apply {
            putExtra(ARG_TRIP_ID, tripId)
        }
    }

    override val viewBinding: ActivityTripDetailsBinding by viewBinding(ActivityTripDetailsBinding::inflate)
    override val viewModel: TripDetailsViewModel by viewModels()

    private lateinit var map: GoogleMap

    private var vmState: TripDetailsState? = null

    private val tripDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(TRIP_DATE_FORMAT, Locale.getDefault())

    private val exportTripDataUriRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        activityResult.data?.data?.let { uri ->
            runCatching { contentResolver.openOutputStream(uri) }.getOrNull()?.let {
                viewModel.onZipOutputAcquired(uri.toString(), it)
            }
        }
    }

    override fun renderState(state: TripDetailsState) {
        vmState = state
        with(viewBinding) {
            state.tripId?.let { setToolbar(toolbar = toolbar, titleText = getString(R.string.trip_id, it, state.tripName.orEmpty())) }
            if (state.startTime != null && state.endTime != null) {
                val startTime =
                    tripDateFormat.format(state.startTime.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime())
                val endTime = tripDateFormat.format(state.endTime.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime())
                val duration = (state.endTime.minus(state.startTime)).toIsoString().substring(2)
                tripDate.text = getString(R.string.from_to_time_minutes, startTime, endTime, duration)
            }
            setMapData(state.locations)
            chartTabs.getTabAt(chartTabs.selectedTabPosition)?.let { tab ->
                val chartTab = ChartTab.values().first { getString(it.stringRes).equals(tab.text.toString(), true) }
                setGraph(chartTab, state)
            }
        }
    }

    override fun handleEvent(event: TripDetailsEvent) {
        when (event) {
            is TripDetailsEvent.AskForDocument -> exportTripDataZipCreateDocument(event.name)
            is TripDetailsEvent.OpenDocument -> openTripExportDocument(event.uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.initGraphs()
        (supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        this.map = map

        map.isMyLocationEnabled = false
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true

        val tripId = intent.getLongExtra(ARG_TRIP_ID, -1)

        viewModel.getTrip(tripId)

        viewBinding.exportTripButton.setOnClickListener {
            viewModel.onExportTrip()
        }
    }

    private fun setMapData(locations: List<GpsData>) {
        if (locations.size > 1) {
            locations.forEachIndexed { index, gpsData ->
                if (index != locations.lastIndex) {
                    map.addPolyline(
                        PolylineOptions()
                            .add(
                                LatLng(gpsData.latitude, gpsData.longitude),
                                LatLng(locations[index + 1].latitude, locations[index + 1].longitude)
                            )
                            .width(8f)
                            .color(ContextCompat.getColor(this, R.color.green))
                    )
                }
            }
            val start = LatLng(locations.first().latitude, locations.first().longitude)
            val end = LatLng(locations.last().latitude, locations.last().longitude)
            map.addMarker(MarkerOptions().position(start).title(getString(R.string.start)))
            map.addMarker(MarkerOptions().position(end).title(getString(R.string.end)))
            val middle = LatLng((start.latitude + end.latitude) / 2.0, (start.longitude + end.longitude) / 2.0)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(middle, MAP_ZOOM_LEVEL))
        } else if (locations.size == 1) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(locations.first().latitude, locations.first().longitude),
                    MAP_ZOOM_LEVEL
                )
            )
        }
    }

    private fun ActivityTripDetailsBinding.initGraphs() {
        ChartTab.values().forEach {
            chartTabs.addTab(chartTabs.newTab().setText(it.stringRes))
        }
        chartTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val chartTab = ChartTab.values().first { getString(it.stringRes).equals(tab?.text.toString(), true) }
                vmState?.let { setGraph(chartTab, it) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
        chart.configureForApp(null)
    }

    private fun LineChart.initData(@StringRes title: Int) {
        val x = SimpleLineDataSet(dataLabel = getString(X_LABEL), lineColor = Color.RED)
        val y = SimpleLineDataSet(dataLabel = getString(Y_LABEL), lineColor = Color.GREEN)
        val z = SimpleLineDataSet(dataLabel = getString(Z_LABEL), lineColor = Color.BLUE)
        description = Description().apply { text = getString(title) }
        data = LineData(x, y, z)
    }

    private fun setGraph(chartTab: ChartTab, state: TripDetailsState) = with(viewBinding) {
        when (chartTab) {
            ChartTab.HEART_RATE -> {
                chart.apply {
                    val x = SimpleLineDataSet(dataLabel = getString(HEART_RATE_LABEL), lineColor = Color.RED)
                    description = Description().apply { text = getString(R.string.heart_rate) }
                    data = LineData(x)
                    state.heartRate?.let { setData(it) }
                }
            }
            ChartTab.ECG -> {
                chart.apply {
                    val x = SimpleLineDataSet(dataLabel = getString(ECG_LABEL), lineColor = Color.RED)
                    description = Description().apply { text = getString(R.string.ecg) }
                    data = LineData(x)
                    state.ecg?.let { setData(it) }
                }
            }
            ChartTab.ACCELEROMETER -> {
                chart.initData(R.string.accelerometer)
                state.accelerometer?.let { chart.setData(it) }
            }
            ChartTab.GRAVITY -> {
                chart.initData(R.string.gravity)
                state.gravity?.let { chart.setData(it) }
            }
            ChartTab.GYROSCOPE -> {
                chart.initData(R.string.gyroscope)
                state.gyroscope?.let { chart.setData(it) }
            }
            ChartTab.LINEAR_ACCELERATION -> {
                chart.initData(R.string.linear_acceleration)
                state.linearAcceleration?.let { chart.setData(it) }
            }
            ChartTab.ROTATION_VECTOR -> {
                chart.apply {
                    val x = SimpleLineDataSet(dataLabel = getString(X_LABEL), lineColor = Color.RED)
                    val y = SimpleLineDataSet(dataLabel = getString(Y_LABEL), lineColor = Color.GREEN)
                    val z = SimpleLineDataSet(dataLabel = getString(Z_LABEL), lineColor = Color.BLUE)
                    val scalar = SimpleLineDataSet(dataLabel = getString(SCALAR_LABEL), lineColor = Color.YELLOW)
                    description = Description().apply { text = getString(R.string.rotation_vector) }
                    data = LineData(x, y, z, scalar)
                }
                state.rotationVector?.let { chart.setData(it) }
            }
        }
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

    private enum class ChartTab(@StringRes val stringRes: Int) {
        HEART_RATE(R.string.heart_rate),
        ECG(R.string.ecg),
        ACCELEROMETER(R.string.accelerometer),
        GRAVITY(R.string.gravity),
        GYROSCOPE(R.string.gyroscope),
        LINEAR_ACCELERATION(R.string.linear_acceleration),
        ROTATION_VECTOR(R.string.rotation_vector),
    }

    private fun exportTripDataZipCreateDocument(name: String) {
        val createZip = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            addCategory(Intent.CATEGORY_OPENABLE)
            type = ALL_MIME_TYPE
            putExtra(Intent.EXTRA_TITLE, name)
        }
        exportTripDataUriRequest.launch(createZip)
    }

    private fun openTripExportDocument(uri: String) {
        startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_VIEW, uri.toUri()).apply {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    data = uri.toUri()
                },
                null
            )
        )
    }
}
