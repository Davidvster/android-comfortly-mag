package com.dv.comfortly.domain.repositories

import com.dv.comfortly.data.raw.observers.LinearAccelerationSensorObserver
import com.dv.comfortly.domain.models.LinearAccelerometerData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LinearAccelerometerSensorRepository
    @Inject
    constructor(
        private val linearAccelerationSensorObserver: LinearAccelerationSensorObserver,
    ) : SensorRepository.LinearAcceleration {
        override fun observeData(): Flow<LinearAccelerometerData> =
            linearAccelerationSensorObserver.observe().map { event ->
                LinearAccelerometerData(
                    xAxisLinearAcceleration = event.values[0],
                    yAxisLinearAcceleration = event.values[1],
                    zAxisLinearAcceleration = event.values[2],
                )
            }
    }
