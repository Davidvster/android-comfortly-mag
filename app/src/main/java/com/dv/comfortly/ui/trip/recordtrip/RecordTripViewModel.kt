package com.dv.comfortly.ui.trip.recordtrip

import android.os.CountDownTimer
import androidx.lifecycle.SavedStateHandle
import com.dv.comfortly.domain.models.GpsData
import com.dv.comfortly.domain.models.SensorData
import com.dv.comfortly.domain.usecases.RecordEcgSensorDataUseCase
import com.dv.comfortly.domain.usecases.RecordSensorDataUseCase
import com.dv.comfortly.domain.usecases.params.RecordSensorDataParams
import com.dv.comfortly.ui.base.BaseViewModel
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class RecordTripViewModel @Inject constructor(
    private val sensorDataUseCase: RecordSensorDataUseCase,
    private val ecgSensorDataUseCase: RecordEcgSensorDataUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<NewTripState, RecordTripEvent>(NewTripState()) {

    companion object {
        private val CALIBRATION_TIME = 2.minutes
        private val CALIBRATION_INTERVAL = 1.seconds
        private val INITIAL_DELAY = 2.seconds
    }

    private var tripId: Long = 0
    private var recordTripType: RecordTripType = RecordTripType.TEST

    private val sensorData = mutableListOf<SensorData>()
    private var accelerometerData = GraphData(emptyList(), emptyList(), emptyList())
    private var gravityData = GraphData(emptyList(), emptyList(), emptyList())
    private var gyroscopeData = GraphData(emptyList(), emptyList(), emptyList())
    private var linearAccelerationData = GraphData(emptyList(), emptyList(), emptyList())
    private var rotationVectorData = RotationVectorGraphData(emptyList(), emptyList(), emptyList(), emptyList())
    private val gpsData = mutableListOf<GpsData>()
    private var heartRateData = HeartRateGraphData(emptyList())
    private var ecgData = EcgGraphData(emptyList())

    private val calibrationCountDownTimer: CountDownTimer by lazy {
        object : CountDownTimer(CALIBRATION_TIME.inWholeMilliseconds, CALIBRATION_INTERVAL.inWholeMilliseconds) {
            override fun onTick(millisUntilFinished: Long) {
                viewState = viewState.copy(
                    isForCalibration = true,
                    calibrationTime = millisUntilFinished.milliseconds
                )
            }

            override fun onFinish() {
                viewState = viewState.copy(
                    isForCalibration = true,
                    calibrationTime = 0.seconds
                )
                onStopAction()
            }
        }
    }

    fun getTripData(tripId: Long, recordTripType: RecordTripType) {
        this.tripId = tripId
        this.recordTripType = recordTripType
        if (recordTripType == RecordTripType.CALIBRATE) {
            calibrationCountDownTimer.start()
        }
        launch {
            delay(INITIAL_DELAY)
            launch {
                sensorDataUseCase(RecordSensorDataParams(tripId, recordTripType)).collect {
                    val currentXvalue = sensorData.size.toFloat()
                    val currentSensorData = it.sensorData
                    sensorData += currentSensorData
                    accelerometerData = accelerometerData.copy(
                        xAxis = accelerometerData.xAxis + Entry(currentXvalue, currentSensorData.accelerometerData.xAxisAcceleration),
                        yAxis = accelerometerData.yAxis + Entry(currentXvalue, currentSensorData.accelerometerData.yAxisAcceleration),
                        zAxis = accelerometerData.zAxis + Entry(currentXvalue, currentSensorData.accelerometerData.zAxisAcceleration),
                    )
                    gravityData = gravityData.copy(
                        xAxis = gravityData.xAxis + Entry(currentXvalue, currentSensorData.gravityData.xAxisGravity),
                        yAxis = gravityData.yAxis + Entry(currentXvalue, currentSensorData.gravityData.yAxisGravity),
                        zAxis = gravityData.zAxis + Entry(currentXvalue, currentSensorData.gravityData.zAxisGravity),
                    )
                    gyroscopeData = gyroscopeData.copy(
                        xAxis = gyroscopeData.xAxis + Entry(currentXvalue, currentSensorData.gyroscopeData.xAxisRotationRate),
                        yAxis = gyroscopeData.yAxis + Entry(currentXvalue, currentSensorData.gyroscopeData.yAxisRotationRate),
                        zAxis = gyroscopeData.zAxis + Entry(currentXvalue, currentSensorData.gyroscopeData.zAxisRotationRate),
                    )
                    linearAccelerationData = linearAccelerationData.copy(
                        xAxis = linearAccelerationData.xAxis + Entry(
                            currentXvalue,
                            currentSensorData.linearAccelerometerData.xAxisLinearAcceleration
                        ),
                        yAxis = linearAccelerationData.yAxis + Entry(
                            currentXvalue,
                            currentSensorData.linearAccelerometerData.yAxisLinearAcceleration
                        ),
                        zAxis = linearAccelerationData.zAxis + Entry(
                            currentXvalue,
                            currentSensorData.linearAccelerometerData.zAxisLinearAcceleration
                        ),
                    )
                    rotationVectorData = rotationVectorData.copy(
                        xAxis = rotationVectorData.xAxis + Entry(currentXvalue, currentSensorData.rotationVectorData.xAxisRotationVector),
                        yAxis = rotationVectorData.yAxis + Entry(currentXvalue, currentSensorData.rotationVectorData.yAxisRotationVector),
                        zAxis = rotationVectorData.zAxis + Entry(currentXvalue, currentSensorData.rotationVectorData.zAxisRotationVector),
                        scalar = rotationVectorData.scalar + Entry(currentXvalue, currentSensorData.rotationVectorData.rotationVectorScalar),
                    )
                    gpsData += it.gpsData
                    heartRateData = heartRateData.copy(
                        heartRate = heartRateData.heartRate + Entry(currentXvalue, it.heartRateData.heartRate.toFloat())
                    )

                    viewState = viewState.copy(
                        accelerometer = accelerometerData,
                        gravity = gravityData,
                        gyroscope = gyroscopeData,
                        linearAcceleration = linearAccelerationData,
                        rotationVector = rotationVectorData,
                        locations = gpsData.distinct(),
                        heartRate = heartRateData,
                        ecgData = ecgData
                    )
                }
            }
            launch {
                ecgSensorDataUseCase(RecordSensorDataParams(tripId, recordTripType)).collect { ecg ->
                    val currentXvalue = ecgData.ecg.size.toFloat()
                    ecgData = ecgData.copy(
                        ecg = ecgData.ecg + ecg.mapIndexed { index, value -> Entry(currentXvalue + index, value.value.toFloat()) }
                    )
                }
            }
        }
    }

    fun onStopAction() {
        emitEvent(
            when (recordTripType) {
                RecordTripType.TEST -> RecordTripEvent.NavigateToCalibrateTrip(tripId)
                RecordTripType.CALIBRATE -> RecordTripEvent.NavigateToRecordTrip(tripId)
                RecordTripType.RECORD -> RecordTripEvent.NavigateToQuestionnaire(tripId)
            }
        )
    }
}
