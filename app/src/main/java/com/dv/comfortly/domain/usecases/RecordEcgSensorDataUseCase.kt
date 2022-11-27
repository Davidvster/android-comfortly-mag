package com.dv.comfortly.domain.usecases

import com.dv.comfortly.data.raw.db.repository.TripEcgCalibrationDataRepository
import com.dv.comfortly.data.raw.db.repository.TripEcgDataRepository
import com.dv.comfortly.domain.models.EcgDataSample
import com.dv.comfortly.domain.repositories.HeartRateRepository
import com.dv.comfortly.domain.usecases.params.RecordSensorDataParams
import com.dv.comfortly.ui.trip.recordtrip.RecordTripType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

interface RecordEcgSensorDataUseCase : BaseFlowUseCase.InputOutput<RecordSensorDataParams, List<EcgDataSample>> {

    class Default @Inject constructor(
        private val heartRateRepository: HeartRateRepository,
        private val tripEcgCalibrationDataRepository: TripEcgCalibrationDataRepository,
        private val tripEcgDataRepository: TripEcgDataRepository
    ) : RecordEcgSensorDataUseCase {

        companion object {
            private const val SAMPLE_RATE_IN_HERTZ_TO_MILLIS = 1000
        }

        override operator fun invoke(input: RecordSensorDataParams): Flow<List<EcgDataSample>> =
            heartRateRepository.observeEcg().map { data ->
                val oneSampleMillis = SAMPLE_RATE_IN_HERTZ_TO_MILLIS / data.sampleRate
                data.samples.mapIndexed { index, value ->
                    val sample = EcgDataSample(
                        tripId = input.tripId,
                        timestamp = data.lastSampleTimestamp - ((data.samples.lastIndex - index) * oneSampleMillis).milliseconds,
                        value = value
                    )
                    when (input.recordTripType) {
                        RecordTripType.TEST -> Unit
                        RecordTripType.CALIBRATE -> tripEcgCalibrationDataRepository.insert(sample)
                        RecordTripType.RECORD -> tripEcgDataRepository.insert(sample)
                    }
                    sample
                }
            }
    }
}
