package com.dv.comfortly.domain.repositories

import com.dv.comfortly.data.raw.observers.AccelerometerSensorObserver
import com.dv.comfortly.domain.models.AccelerometerData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccelerometerSensorRepository
    @Inject
    constructor(
        private val accelerometerSensorObserver: AccelerometerSensorObserver,
    ) : SensorRepository.Accelerometer {
        override fun observeData(): Flow<AccelerometerData> =
            accelerometerSensorObserver.observe().map { d ->
                AccelerometerData(
                    xAxisAcceleration = d.event.values[0],
                    yAxisAcceleration = d.event.values[1],
                    zAxisAcceleration = d.event.values[2],
                    accuracy = d.accuracy,
                )
            }
    }
