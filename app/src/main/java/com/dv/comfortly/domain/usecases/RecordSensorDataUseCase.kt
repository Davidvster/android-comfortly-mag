package com.dv.comfortly.domain.usecases

import com.dv.comfortly.data.raw.db.repository.TripCalibrationDatapointRepository
import com.dv.comfortly.data.raw.db.repository.TripDatapointRepository
import com.dv.comfortly.domain.models.AccelerometerData
import com.dv.comfortly.domain.models.GpsData
import com.dv.comfortly.domain.models.GravityData
import com.dv.comfortly.domain.models.GyroscopeData
import com.dv.comfortly.domain.models.HeartRateData
import com.dv.comfortly.domain.models.LinearAccelerometerData
import com.dv.comfortly.domain.models.RotationVectorData
import com.dv.comfortly.domain.models.SensorData
import com.dv.comfortly.domain.models.TripDatapoint
import com.dv.comfortly.domain.repositories.GpsRepository
import com.dv.comfortly.domain.repositories.HeartRateRepository
import com.dv.comfortly.domain.repositories.SensorRepository
import com.dv.comfortly.domain.usecases.params.RecordSensorDataParams
import com.dv.comfortly.ui.trip.recordtrip.RecordTripType
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

interface RecordSensorDataUseCase : BaseFlowUseCase.InputOutput<RecordSensorDataParams, TripDatapoint> {

    class Default @Inject constructor(
        private val accelerometerSensorRepository: SensorRepository.Accelerometer,
        private val gravitySensorRepository: SensorRepository.Gravity,
        private val gyroscopeSensorRepository: SensorRepository.Gyroscope,
        private val linearAccelerometerSensorRepository: SensorRepository.LinearAcceleration,
        private val rotationVectorSensorRepository: SensorRepository.RotationVector,
        private val gpsRepository: GpsRepository,
        private val heartRateRepository: HeartRateRepository,
        private val tripCalibrationDatapointRepository: TripCalibrationDatapointRepository,
        private val tripDatapointRepository: TripDatapointRepository
    ) : RecordSensorDataUseCase {

        companion object {
            private val DATA_DEBOUNCE = 5.milliseconds
        }

        @OptIn(FlowPreview::class)
        override operator fun invoke(input: RecordSensorDataParams): Flow<TripDatapoint> = combine(
            accelerometerSensorRepository.observeData(),
            gravitySensorRepository.observeData(),
            gyroscopeSensorRepository.observeData(),
            linearAccelerometerSensorRepository.observeData(),
            rotationVectorSensorRepository.observeData(),
            gpsRepository.observeData(),
            heartRateRepository.observeHeartRate()
        ) { data ->
            TripDatapoint(
                tripId = input.tripId,
                timestamp = Clock.System.now(),
                sensorData = SensorData(
                    accelerometerData = data[0] as AccelerometerData,
                    gravityData = data[1] as GravityData,
                    gyroscopeData = data[2] as GyroscopeData,
                    linearAccelerometerData = data[3] as LinearAccelerometerData,
                    rotationVectorData = data[4] as RotationVectorData,
                ),
                gpsData = data[5] as GpsData,
                heartRateData = data[6] as HeartRateData
            ).also { tripDatapoint ->
                when (input.recordTripType) {
                    RecordTripType.TEST -> Unit
                    RecordTripType.CALIBRATE -> tripCalibrationDatapointRepository.insert(tripDatapoint)
                    RecordTripType.RECORD -> tripDatapointRepository.insert(tripDatapoint)
                }
            }
        }.debounce(DATA_DEBOUNCE)
    }
}
