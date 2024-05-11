package com.dv.comfortly.domain.repositories

import android.hardware.SensorManager
import com.dv.comfortly.data.raw.observers.GyroscopeSensorObserver
import com.dv.comfortly.domain.models.GyroscopeData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GyroscopeSensorRepository
@Inject
constructor(
    private val gyroscopeSensorObserver: GyroscopeSensorObserver,
) : SensorRepository.Gyroscope {

    companion object {
        private const val EPSILON = 0.00000001f

        // Create a constant to convert nanoseconds to seconds.
        private const val NS2S = 1.0f / 1000000000.0f
        private val deltaRotationVector = FloatArray(4) { 0f }
    }

    private var timestamp: Float = 0f

    override fun observeData(): Flow<GyroscopeData> =
        gyroscopeSensorObserver.observe().onStart {
            timestamp = 0f
        }.map { d ->
            // This timestep's delta rotation to be multiplied by the current rotation
            // after computing it from the gyro sample data.
            if (timestamp != 0f) {
                val dT = (d.event.timestamp - timestamp) * NS2S
                // Axis of the rotation sample, not normalized yet.
                var axisX: Float = d.event.values[0]
                var axisY: Float = d.event.values[1]
                var axisZ: Float = d.event.values[2]

                // Calculate the angular speed of the sample
                val omegaMagnitude: Float = sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ)

                // Normalize the rotation vector if it's big enough to get the axis
                // (that is, EPSILON should represent your maximum allowable margin of error)
                if (omegaMagnitude > EPSILON) {
                    axisX /= omegaMagnitude
                    axisY /= omegaMagnitude
                    axisZ /= omegaMagnitude
                }

                // Integrate around this axis with the angular speed by the timestep
                // in order to get a delta rotation from this sample over the timestep
                // We will convert this axis-angle representation of the delta rotation
                // into a quaternion before turning it into the rotation matrix.
                val thetaOverTwo: Float = omegaMagnitude * dT / 2.0f
                val sinThetaOverTwo: Float = sin(thetaOverTwo)
                val cosThetaOverTwo: Float = cos(thetaOverTwo)
                deltaRotationVector[0] = sinThetaOverTwo * axisX
                deltaRotationVector[1] = sinThetaOverTwo * axisY
                deltaRotationVector[2] = sinThetaOverTwo * axisZ
                deltaRotationVector[3] = cosThetaOverTwo
            }
            timestamp = d.event.timestamp.toFloat()
            val deltaRotationMatrix = FloatArray(9) { 0f }
            SensorManager.getRotationMatrixFromVector(
                deltaRotationMatrix,
                deltaRotationVector
            )
            // User code should concatenate the delta rotation we computed with the current rotation
            // in order to get the updated rotation.
            // val rotationCurrent = rotationCurrent * deltaRotationMatrix

            val orientations = FloatArray(3)
            SensorManager.getOrientation(deltaRotationMatrix, orientations)
            GyroscopeData(
                xAxisRotationRate = d.event.values[0],
                yAxisRotationRate = d.event.values[1],
                zAxisRotationRate = d.event.values[2],
                orientationX = Math.toDegrees(orientations[0].toDouble()).toFloat(),
                orientationY = Math.toDegrees(orientations[1].toDouble()).toFloat(),
                orientationZ = Math.toDegrees(orientations[2].toDouble()).toFloat(),
                accuracy = d.accuracy
            )
        }
}
