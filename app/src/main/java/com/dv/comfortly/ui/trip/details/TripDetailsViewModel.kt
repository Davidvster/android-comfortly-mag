package com.dv.comfortly.ui.trip.details

import androidx.lifecycle.SavedStateHandle
import com.dv.comfortly.domain.models.Trip
import com.dv.comfortly.domain.usecases.ExportTripToZipUseCase
import com.dv.comfortly.domain.usecases.LoadTripUseCase
import com.dv.comfortly.domain.usecases.params.ExportTripParams
import com.dv.comfortly.ui.base.BaseViewModel
import com.dv.comfortly.ui.trip.recordtrip.EcgGraphData
import com.dv.comfortly.ui.trip.recordtrip.GraphData
import com.dv.comfortly.ui.trip.recordtrip.HeartRateGraphData
import com.dv.comfortly.ui.trip.recordtrip.RotationVectorGraphData
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.io.OutputStream
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    private val loadTripUseCase: LoadTripUseCase,
    private val exportTripToZipUseCase: ExportTripToZipUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<TripDetailsState, TripDetailsEvent>(TripDetailsState()) {

    companion object {
        private const val EXPORT_NAME_FORMAT = "%d_%s_%s.zip"
        private const val TRIP_DATE_EXPORT_NAME_FORMAT = "dd-MM-yyyy_HH-mm"
    }

    private val tripDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(TRIP_DATE_EXPORT_NAME_FORMAT, Locale.getDefault())

    private var tripId: Long = -1

    private var trip: Trip? = null

    fun getTrip(tripId: Long) {
        this.tripId = tripId
        launchWithBlockingLoading {
            loadTripUseCase(tripId).also { trip ->
                this@TripDetailsViewModel.trip = trip

                viewState = TripDetailsState(
                    tripId = trip.id,
                    tripName = trip.name,
                    startTime = trip.data.firstOrNull()?.timestamp,
                    endTime = trip.data.lastOrNull()?.timestamp,
                    accelerometer = GraphData(
                        xAxis = trip.data.map { it.sensorData.accelerometerData.xAxisAcceleration }.toEntry(),
                        yAxis = trip.data.map { it.sensorData.accelerometerData.yAxisAcceleration }.toEntry(),
                        zAxis = trip.data.map { it.sensorData.accelerometerData.zAxisAcceleration }.toEntry(),
                    ),
                    gravity = GraphData(
                        xAxis = trip.data.map { it.sensorData.gravityData.xAxisGravity }.toEntry(),
                        yAxis = trip.data.map { it.sensorData.gravityData.yAxisGravity }.toEntry(),
                        zAxis = trip.data.map { it.sensorData.gravityData.zAxisGravity }.toEntry(),
                    ),
                    gyroscope = GraphData(
                        xAxis = trip.data.map { it.sensorData.gyroscopeData.xAxisRotationRate }.toEntry(),
                        yAxis = trip.data.map { it.sensorData.gyroscopeData.yAxisRotationRate }.toEntry(),
                        zAxis = trip.data.map { it.sensorData.gyroscopeData.zAxisRotationRate }.toEntry(),
                    ),
                    linearAcceleration = GraphData(
                        xAxis = trip.data.map { it.sensorData.linearAccelerometerData.xAxisLinearAcceleration }.toEntry(),
                        yAxis = trip.data.map { it.sensorData.linearAccelerometerData.yAxisLinearAcceleration }.toEntry(),
                        zAxis = trip.data.map { it.sensorData.linearAccelerometerData.zAxisLinearAcceleration }.toEntry(),
                    ),
                    rotationVector = RotationVectorGraphData(
                        xAxis = trip.data.map { it.sensorData.rotationVectorData.xAxisRotationVector }.toEntry(),
                        yAxis = trip.data.map { it.sensorData.rotationVectorData.yAxisRotationVector }.toEntry(),
                        zAxis = trip.data.map { it.sensorData.rotationVectorData.zAxisRotationVector }.toEntry(),
                        scalar = trip.data.map { it.sensorData.rotationVectorData.rotationVectorScalar }.toEntry(),
                    ),
                    locations = trip.data.map { it.gpsData }.distinct(),
                    heartRate = HeartRateGraphData(trip.data.map { it.heartRateData.heartRate.toFloat() }.toEntry()),
                    ecg = EcgGraphData(trip.ecgData.map { it.value.toFloat() }.toEntry())
                )
            }
        }
    }

    fun onExportTrip() {
        trip?.let {
            val date = tripDateFormat.format(
                it.data.first().timestamp.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
            )
            val name = String.format(EXPORT_NAME_FORMAT, it.id, it.name, date)
            emitEvent(TripDetailsEvent.AskForDocument(name))
        }
    }

    fun onZipOutputAcquired(uri: String, outputStream: OutputStream) {
        launchWithBlockingLoading {
            exportTripToZipUseCase(ExportTripParams(tripId, outputStream))
            emitEvent(TripDetailsEvent.OpenDocument(uri))
        }
    }

    private fun List<Float>.toEntry() = mapIndexed { index, data -> Entry(index.toFloat(), data) }
}
