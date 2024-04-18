package com.dv.comfortly.ui.trip.recordtrip

import android.os.CountDownTimer
import androidx.lifecycle.SavedStateHandle
import com.dv.comfortly.domain.models.GpsData
import com.dv.comfortly.domain.usecases.RecordEcgSensorDataUseCase
import com.dv.comfortly.domain.usecases.RecordSensorDataUseCase
import com.dv.comfortly.domain.usecases.params.RecordSensorDataParams
import com.dv.comfortly.ui.base.BaseViewModel
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class RecordTripViewModel
    @Inject
    constructor(
        private val sensorDataUseCase: RecordSensorDataUseCase,
        private val ecgSensorDataUseCase: RecordEcgSensorDataUseCase,
        private val savedStateHandle: SavedStateHandle,
    ) : BaseViewModel<NewTripState, RecordTripEvent>(NewTripState()) {
        companion object {
            private const val MAX_UI_DATA_ITEMS = 300
            private val CALIBRATION_TIME = 2.minutes
            private val CALIBRATION_INTERVAL = 1.seconds
            private val INITIAL_DELAY = 2.seconds
        }

        private var tripId: Long = 0
        private var recordTripType: RecordTripType = RecordTripType.TEST

        private var sensorDataIndex = 0
        private var ecgSensorDataIndex = 0
        private var accelerometerData = GraphData(emptyList(), emptyList(), emptyList())
        private var gravityData = GraphData(emptyList(), emptyList(), emptyList())
        private var gyroscopeData = GraphData(emptyList(), emptyList(), emptyList())
        private var linearAccelerationData = GraphData(emptyList(), emptyList(), emptyList())
        private var rotationVectorData = RotationVectorGraphData(emptyList(), emptyList(), emptyList(), emptyList())
        private var gpsData = emptyList<GpsData>()
        private var heartRateData = HeartRateGraphData(emptyList())
        private var ecgData = EcgGraphData(emptyList())
        private val startDateTime = Clock.System.now()

        private val calibrationCountDownTimer: CountDownTimer by lazy {
            object : CountDownTimer(CALIBRATION_TIME.inWholeMilliseconds, CALIBRATION_INTERVAL.inWholeMilliseconds) {
                override fun onTick(millisUntilFinished: Long) {
                    viewState = viewState.copy(calibrationTime = millisUntilFinished.milliseconds)
                }

                override fun onFinish() {
                    viewState = viewState.copy(calibrationTime = 0.seconds)
                    onStopAction()
                }
            }
        }

        fun getTripData(
            tripId: Long,
            recordTripType: RecordTripType,
        ) {
            this.tripId = tripId
            this.recordTripType = recordTripType
            viewState = viewState.copy(recordTripType = recordTripType)
            when (recordTripType) {
                RecordTripType.TEST,
                RecordTripType.RECORD,
                -> Unit
                RecordTripType.CALIBRATE -> calibrationCountDownTimer.start()
            }
            launch {
                delay(INITIAL_DELAY)
                launch {
                    sensorDataUseCase(RecordSensorDataParams(tripId, recordTripType)).flowOn(Dispatchers.IO).collect {
                        sensorDataIndex = accelerometerData.xAxis.size
                        val currentXvalue = sensorDataIndex.toFloat()
                        val currentSensorData = it.sensorData
//                    sensorDataIndex++
                        accelerometerData =
                            accelerometerData.copy(
                                xAxis =
                                    accelerometerData.xAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.accelerometerData.xAxisAcceleration,
                                        ),
                                    ),
                                yAxis =
                                    accelerometerData.yAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.accelerometerData.yAxisAcceleration,
                                        ),
                                    ),
                                zAxis =
                                    accelerometerData.zAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.accelerometerData.zAxisAcceleration,
                                        ),
                                    ),
                            )
                        gravityData =
                            gravityData.copy(
                                xAxis = gravityData.xAxis.appendWithLimitSize(Entry(currentXvalue, currentSensorData.gravityData.xAxisGravity)),
                                yAxis = gravityData.yAxis.appendWithLimitSize(Entry(currentXvalue, currentSensorData.gravityData.yAxisGravity)),
                                zAxis = gravityData.zAxis.appendWithLimitSize(Entry(currentXvalue, currentSensorData.gravityData.zAxisGravity)),
                            )
                        gyroscopeData =
                            gyroscopeData.copy(
                                xAxis =
                                    gyroscopeData.xAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.gyroscopeData.xAxisRotationRate,
                                        ),
                                    ),
                                yAxis =
                                    gyroscopeData.yAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.gyroscopeData.yAxisRotationRate,
                                        ),
                                    ),
                                zAxis =
                                    gyroscopeData.zAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.gyroscopeData.zAxisRotationRate,
                                        ),
                                    ),
                            )
                        linearAccelerationData =
                            linearAccelerationData.copy(
                                xAxis =
                                    linearAccelerationData.xAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.linearAccelerometerData.xAxisLinearAcceleration,
                                        ),
                                    ),
                                yAxis =
                                    linearAccelerationData.yAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.linearAccelerometerData.yAxisLinearAcceleration,
                                        ),
                                    ),
                                zAxis =
                                    linearAccelerationData.zAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.linearAccelerometerData.zAxisLinearAcceleration,
                                        ),
                                    ),
                            )
                        rotationVectorData =
                            rotationVectorData.copy(
                                xAxis =
                                    rotationVectorData.xAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.rotationVectorData.xAxisRotationVector,
                                        ),
                                    ),
                                yAxis =
                                    rotationVectorData.yAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.rotationVectorData.yAxisRotationVector,
                                        ),
                                    ),
                                zAxis =
                                    rotationVectorData.zAxis.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.rotationVectorData.zAxisRotationVector,
                                        ),
                                    ),
                                scalar =
                                    rotationVectorData.scalar.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            currentSensorData.rotationVectorData.rotationVectorScalar,
                                        ),
                                    ),
                            )
                        gpsData = gpsData.appendWithLimitSize(it.gpsData)
                        heartRateData =
                            heartRateData.copy(
                                heartRate =
                                    heartRateData.heartRate.appendWithLimitSize(
                                        Entry(
                                            currentXvalue,
                                            it.heartRateData.heartRate.toFloat(),
                                        ),
                                    ),
                            )

                        viewState =
                            viewState.copy(
                                accelerometer = accelerometerData,
                                gravity = gravityData,
                                gyroscope = gyroscopeData,
                                linearAcceleration = linearAccelerationData,
                                rotationVector = rotationVectorData,
                                locations = gpsData.distinct(),
                                heartRate = heartRateData,
                                ecgData = ecgData,
                                totalElapsedTime = Clock.System.now() - startDateTime,
                            )
                    }
                }
                launch {
                    ecgSensorDataUseCase(RecordSensorDataParams(tripId, recordTripType)).flowOn(Dispatchers.IO).collect { ecg ->
                        ecgSensorDataIndex = ecgData.ecg.size
                        val currentXvalue = ecgSensorDataIndex.toFloat()
//                    ecgSensorDataIndex += ecg.size
                        ecgData =
                            ecgData.copy(
                                ecg =
                                    ecgData.ecg.appendWithLimitSize(
                                        ecg.mapIndexed { index, value ->
                                            Entry(
                                                currentXvalue + index,
                                                value.value.toFloat(),
                                            )
                                        },
                                    ),
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
                },
            )
        }

        private fun List<GpsData>.appendWithLimitSize(newEntry: GpsData): List<GpsData> =
            if (size > MAX_UI_DATA_ITEMS) {
                this.subList(size - MAX_UI_DATA_ITEMS, size) + newEntry
            } else {
                this + newEntry
            }

        private fun List<Entry>.appendWithLimitSize(newEntry: Entry): List<Entry> =
            (
                if (size > MAX_UI_DATA_ITEMS) {
                    this.subList(size - MAX_UI_DATA_ITEMS, size) + newEntry
                } else {
                    this + newEntry
                }
            ).mapIndexes()

        private fun List<Entry>.appendWithLimitSize(newEntries: List<Entry>): List<Entry> =
            (
                if (size + newEntries.size > MAX_UI_DATA_ITEMS) {
                    this.subList((size + newEntries.size) - MAX_UI_DATA_ITEMS, size) + newEntries
                } else {
                    this + newEntries
                }
            ).mapIndexes()

        private fun List<Entry>.mapIndexes() = this.mapIndexed { index, value -> Entry(index.toFloat(), value.y) }
    }
