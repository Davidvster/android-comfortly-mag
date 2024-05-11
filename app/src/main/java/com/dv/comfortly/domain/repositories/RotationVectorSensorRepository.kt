package com.dv.comfortly.domain.repositories

import android.hardware.SensorManager
import com.dv.comfortly.data.raw.observers.RotationVectorSensorObserver
import com.dv.comfortly.domain.models.RotationVectorData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RotationVectorSensorRepository
    @Inject
    constructor(
        private val rotationVectorSensorObserver: RotationVectorSensorObserver,
    ) : SensorRepository.RotationVector {
        override fun observeData(): Flow<RotationVectorData> =
            rotationVectorSensorObserver.observe().map { d ->
                val rotationMatrix = FloatArray(16)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, d.event.values)

                val orientations = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientations)

                RotationVectorData(
                    xAxisRotationVector = d.event.values[0],
                    yAxisRotationVector = d.event.values[1],
                    zAxisRotationVector = d.event.values[2],
                    rotationVectorScalar = d.event.values[3],
                    orientationX = Math.toDegrees(orientations[0].toDouble()).toFloat(),
                    orientationY = Math.toDegrees(orientations[1].toDouble()).toFloat(),
                    orientationZ = Math.toDegrees(orientations[2].toDouble()).toFloat(),
                    accuracy = d.accuracy,
                )
            }
    }
