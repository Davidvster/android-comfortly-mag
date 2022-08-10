package com.dv.comfortly.domain.repositories

import com.dv.comfortly.data.raw.observers.RotationVectorSensorObserver
import com.dv.comfortly.domain.models.RotationVectorData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RotationVectorSensorRepository @Inject constructor(
    private val rotationVectorSensorObserver: RotationVectorSensorObserver
) : SensorRepository.RotationVector {

    override fun observeData(): Flow<RotationVectorData> =
        rotationVectorSensorObserver.observe().map { event ->
            RotationVectorData(
                xAxisRotationVector = event.values[0],
                yAxisRotationVector = event.values[1],
                zAxisRotationVector = event.values[2],
                rotationVectorScalar = event.values[3]
            )
        }
}
