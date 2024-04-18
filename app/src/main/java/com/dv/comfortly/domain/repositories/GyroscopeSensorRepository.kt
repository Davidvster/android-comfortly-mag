package com.dv.comfortly.domain.repositories

import com.dv.comfortly.data.raw.observers.GyroscopeSensorObserver
import com.dv.comfortly.domain.models.GyroscopeData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GyroscopeSensorRepository
    @Inject
    constructor(
        private val gyroscopeSensorObserver: GyroscopeSensorObserver,
    ) : SensorRepository.Gyroscope {
        override fun observeData(): Flow<GyroscopeData> =
            gyroscopeSensorObserver.observe().map { event ->
                GyroscopeData(
                    xAxisRotationRate = event.values[0],
                    yAxisRotationRate = event.values[1],
                    zAxisRotationRate = event.values[2],
                )
            }
    }
