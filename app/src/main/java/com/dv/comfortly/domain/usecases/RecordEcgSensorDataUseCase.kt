package com.dv.comfortly.domain.usecases

import com.dv.comfortly.data.raw.db.repository.TripEcgCalibrationDataRepository
import com.dv.comfortly.data.raw.db.repository.TripEcgDataRepository
import com.dv.comfortly.domain.models.EcgData.Companion.POLAR_ECG_DATA_START
import com.dv.comfortly.domain.models.EcgDataSample
import com.dv.comfortly.domain.repositories.HeartRateRepository
import com.dv.comfortly.domain.usecases.params.RecordSensorDataParams
import com.dv.comfortly.ui.trip.recordtrip.RecordTripType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import javax.inject.Inject
import kotlin.time.Duration.Companion.nanoseconds

interface RecordEcgSensorDataUseCase : BaseFlowUseCase.InputOutput<RecordSensorDataParams, List<EcgDataSample>> {
    class Default
        @Inject
        constructor(
            private val heartRateRepository: HeartRateRepository,
            private val tripEcgCalibrationDataRepository: TripEcgCalibrationDataRepository,
            private val tripEcgDataRepository: TripEcgDataRepository,
        ) : RecordEcgSensorDataUseCase {
            override operator fun invoke(input: RecordSensorDataParams): Flow<List<EcgDataSample>> =
                heartRateRepository.observeEcg().map { data ->
                    data.samples.mapIndexed { index, value ->
                        val sample =
                            EcgDataSample(
                                tripId = input.tripId,
                                timestamp = Instant.parse(POLAR_ECG_DATA_START) + value.timeStamp.nanoseconds,
                                value = value.voltage,
                            )
                        when (input.recordTripType) {
                            RecordTripType.DEMO -> Unit
                            RecordTripType.TEST -> Unit
                            RecordTripType.CALIBRATE -> tripEcgCalibrationDataRepository.insert(sample)
                            RecordTripType.RECORD -> tripEcgDataRepository.insert(sample)
                        }
                        sample
                    }
                }
        }
}
