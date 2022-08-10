package com.dv.comfortly.domain.repositories

import com.dv.comfortly.data.raw.observers.GravitySensorObserver
import com.dv.comfortly.domain.models.GravityData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GravitySensorRepository @Inject constructor(
    private val gravitySensorObserver: GravitySensorObserver
) : SensorRepository.Gravity {

    override fun observeData(): Flow<GravityData> =
        gravitySensorObserver.observe().map { event ->
            GravityData(
                xAxisGravity = event.values[0],
                yAxisGravity = event.values[1],
                zAxisGravity = event.values[2]
            )
        }
}
